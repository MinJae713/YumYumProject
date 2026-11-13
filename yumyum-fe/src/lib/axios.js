import axios from 'axios';

// 백엔드 주소 (본인 환경에 맞게 수정하세요)
const baseURL = 'http://localhost:8080'; 

const instance = axios.create({
  baseURL,
  withCredentials: true, // ⭐️ 쿠키(리프레시 토큰)를 주고받기 위해 필수!
});

// [요청 인터셉터] 모든 요청 헤더에 토큰을 자동으로 실어 보냅니다.
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; // 헤더 키 확인 필요 (Authorization 또는 다른 이름)
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// [응답 인터셉터] 401 발생 시 액세스 토큰을 재발급하고 원본 요청을 재시도합니다.
instance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const url = error.config.url;

    // ✅ 로그인 / refresh 요청은 refresh 로직 제외
    const isAuthRequest =
      url.includes('/api/v1/user/login') ||
      url.includes('/api/v1/user/refresh');

    if (error.response?.status === 401 && 
      !originalRequest._retry && 
      !isAuthRequest) { // auth 요청이 아닌 경우에만 refreshToken이 먹히도록! 이 포인트인듯?
      // ⭐️ 오 난 핵심을 찔렀나??
      originalRequest._retry = true;
      try {
        const refreshResponse = await instance.post('/api/v1/user/refresh');
        const newToken = refreshResponse?.data?.data;

        if (!newToken) throw new Error('토큰 재발급 실패');

        localStorage.setItem('accessToken', newToken);
        originalRequest.headers.Authorization = `Bearer ${newToken}`;

        return instance(originalRequest);
      } catch (refreshError) {
        localStorage.removeItem('accessToken');
        window.location.href = '/auth/login';
        return Promise.reject(refreshError);
      }
    }

    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      window.location.href = '/auth/login';
    }

    return Promise.reject(error);
  }
);

export const contextPath = '/api/v1';

export default instance;
