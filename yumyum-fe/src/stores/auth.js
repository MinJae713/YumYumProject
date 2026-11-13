import { defineStore } from 'pinia';
import axios from '@/lib/axios';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export const useAuthStore = defineStore('auth', () => {
  const router = useRouter();
  
  // 1. 상태 초기화 (새로고침 시 LocalStorage에서 복구)
  const accessToken = ref(localStorage.getItem('accessToken') || null);
  const isAuthenticated = ref(!!accessToken.value);
  
  // LocalStorage에 저장된 JSON 문자열을 객체로 파싱
  const storedUser = localStorage.getItem('user');
  let parsedUser = null;
  if (storedUser) {
    try {
      parsedUser = JSON.parse(storedUser);
    } catch (e) {
      console.error('사용자 정보 파싱 실패:', e);
      localStorage.removeItem('user');
    }
  }
  const user = ref(parsedUser);

  // 2. 로그인 데이터 저장/삭제 헬퍼 함수
  const setAuthData = (token, userData) => {
    accessToken.value = token;
    user.value = userData;
    isAuthenticated.value = !!token;

    if (token) {
      localStorage.setItem('accessToken', token);
    } else {
      localStorage.removeItem('accessToken');
    }

    if (userData) {
      localStorage.setItem('user', JSON.stringify(userData));
    } else {
      localStorage.removeItem('user');
    }
  };

  // 3. 로그인 액션 (실제 백엔드 연동)
  const login = async (email, password) => {
    try {
      // API 호출
      const response = await axios.post('/api/v1/user/login', { email, password });

      console.log('로그인 응답:', response.data);

      // 성공 체크 (code 200)
      if (response.data.code === 200) {
        // 백엔드 응답 구조:
        // { data: { accessToken: "...", user: { userId: 1, name: "...", role: "..." } } }
        const payload = response.data.data; 
        const token = payload.accessToken;
        const rawUser = payload.user;

        if (!token) throw new Error('토큰이 없습니다.');
        if (!rawUser) throw new Error('유저 정보가 없습니다.');

        // ⭐️ 중요: 프론트엔드 편의를 위해 userId를 id로도 매핑하여 저장
        const userData = {
          ...rawUser,
          id: rawUser.userId, // userId 값을 id로 복사 (기존 컴포넌트 호환성 유지)
        };

        // 스토어 및 로컬스토리지에 저장
        setAuthData(token, userData);

        return true; 
      } else {
        alert(response.data.message || '로그인 실패');
        return false;
      }

    } catch (error) {
      console.error('로그인 에러:', error);
      const msg = error?.response?.data?.message || '로그인 중 오류가 발생했습니다.';
      alert(msg);
      return false;
    }
  };

  // 4. 로그아웃 액션
  const logout = async () => {
    try {
      await axios.post('/api/v1/user/logout');
    } catch (e) {
      console.error('로그아웃 요청 실패 (토큰 만료 등):', e);
    } finally {
      // 서버 응답과 상관없이 클라이언트 상태는 무조건 초기화
      setAuthData(null, null);
      router.push('/auth/login');
    }
  };

  return { 
    accessToken, 
    isAuthenticated, 
    user, 
    login, 
    logout,
    setAuthData
  };
});