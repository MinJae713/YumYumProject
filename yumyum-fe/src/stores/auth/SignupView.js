import { defineStore } from "pinia";
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import { useUserApi } from "@/lib/api/user";
import { useAuthStore } from "@/stores/auth";

const useSignupViewStore = defineStore("signupViewStore", () => {
  const router = useRouter();
  const userApi = useUserApi();
  const authStore = useAuthStore();

  const formData = ref({
    email: "",
    password: "",
    confirmPassword: "",
    name: "",
    userType: "general",
  });

  const isDirty = computed(() => Object.values(formData.value).some(v => v !== ""));

  const handleSubmit = async () => {
    // 1. 비밀번호 확인
    if (formData.value.password !== formData.value.confirmPassword) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
      const payload = {
        name: formData.value.name,
        email: formData.value.email,
        password: formData.value.password,
        role: formData.value.userType === "business" ? "BUSINESS" : "USER",
        timezone,
      };

      // 2. API 호출
      // userApi가 이미 response.data를 리턴하므로, response 변수가 곧 JSON 본문입니다.
      const response = await userApi.joinApi(payload);

      console.log("회원가입 응답:", response);

      // 3. 성공 여부 확인 (code: 201)
      if (response && response.code === 201) {
        
        // 데이터 꺼내기 (JSON 구조: response.data 안에 accessToken, user 있음)
        const { accessToken, user } = response.data;

        // 토큰과 유저 정보가 확실히 있는지 체크
        if (accessToken && user) {
          
          // 4. 데이터 매핑 (userId -> id)
          const userData = {
            ...user,
            id: user.userId, 
          };

          // 5. 로그인 처리 (Auth Store에 저장)
          authStore.setAuthData(accessToken, userData);

          // 6. 페이지 이동
          alert("회원가입이 완료되었습니다."); 
          router.push("/auth/onboarding");
          
        } else {
          // 성공 응답은 왔는데 데이터가 비어있는 경우
          console.error("데이터 누락:", response.data);
          alert("회원가입 처리 중 데이터 오류가 발생했습니다.");
        }

      } else {
        // code가 201이 아닌 경우 (실패)
        alert(response?.message || "회원가입에 실패했습니다.");
      }

    } catch (error) {
      console.error("시스템 에러:", error);
      const msg = error?.response?.data?.message || "오류가 발생했습니다.";
      alert(msg);
    }
  };

  const goLogin = () => router.push("/auth/login");

  return {
    formData,
    handleSubmit,
    goLogin,
    isDirty
  };
});

export default useSignupViewStore;