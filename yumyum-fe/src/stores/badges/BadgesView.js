import { defineStore } from "pinia";
import { computed, ref } from "vue";
import { useBadgeApi } from "@/lib/api/badge";
import { parseToCategoryLabel } from "@/utils/badgeUtils";

const useBadgeView = defineStore("badgeView", () => {
  const badgeApi = useBadgeApi();
  // data
  const badges = ref([]);
  const fullCount = ref(0);
  const achievedCount = ref(0);
  const achievedRate = ref(0);

  const selectedCategory = ref("all"); // 뱃지 카테고리
  const filteredBadges = computed(() => // 카테고리 마다 분류
    badges.value.filter((badge) => 
      selectedCategory.value === "all" || 
      badge.category === selectedCategory.value),
  );

  const formatDateKey = (date) => {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, "0"); // 01, 02...
    const day = String(d.getDate()).padStart(2, "0");       // 01, 02...
    return `${year}-${month}-${day}`;
  };

  // api
  const setBadgeViewData = async () => {
    const response = await badgeApi.getBadgeListApi();
    badges.value = [];
    console.log(response.data);
    fullCount.value = response.data.fullCount;
    achievedCount.value = response.data.achievedCount;
    achievedRate.value = Math.round(response.data.achievedRate*100);
    response.data.badges.forEach(badge => badges.value.push({
      id: badge.badgeId,
      name: badge.badgeTitle,
      description: badge.description,
      url: badge.badgeImageUrl,
      category: parseToCategoryLabel(badge.badgeCategoryName),
      achieved: badge.isAchieved,
      achievedDate: formatDateKey(badge.achievedAt)
    }));
  }
  return {
    badges,
    fullCount,
    achievedCount,
    achievedRate,

    selectedCategory,
    filteredBadges,

    setBadgeViewData
  };
});
export default useBadgeView;