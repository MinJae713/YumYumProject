import { defineStore } from "pinia";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";

const useLoginViewStore = defineStore("loginViewStore", () => {
  const router = useRouter();
  const authStore = useAuthStore();

  const email = ref("");
  const password = ref("");
  const isLoading = ref(false);

  const handleLogin = async () => {
    if (!email.value || !password.value) return;
    isLoading.value = true;
    try {
      const success = await authStore.login(email.value, password.value);
      if (success) {
        router.push("/");
      }
    } finally {
      isLoading.value = false;
    }
  };

  const goSignup = () => router.push("/auth/signup");

  return {
    email,
    password,
    isLoading,
    handleLogin,
    goSignup,
  };
});

export default useLoginViewStore;
