<script setup>
import { onMounted } from "vue";
import ProfileCard from "@/components/user/ProfileCard.vue";
import SettingsSection from "@/components/user/SettingsSection.vue";
import useMyPageView from "@/stores/user/MyPageView";

const store = useMyPageView();
onMounted(() => {
  store.fetchUserInfo();
});
</script>

<template>
  <div class="max-w-3xl mx-auto px-4 py-10 space-y-8">
    <ProfileCard />
    <SettingsSection />
  </div>
</template>
