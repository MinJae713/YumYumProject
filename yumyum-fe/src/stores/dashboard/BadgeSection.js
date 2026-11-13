import { formatDateKey } from "@/utils/commonUtils";
import { defineStore } from "pinia";
import { ref } from "vue";

const useBadgeSection = defineStore("badgeSection", () => {
  const achievedBadges = ref([]);

  const setBadgeSectionData = (recentBadgeList) => {
    achievedBadges.value = [];
    recentBadgeList.forEach(badge => achievedBadges.value.push({
      id: badge.badgeId,
      name: badge.badgeTitle,
      badgeImageUrl: badge.badgeImageUrl,
      description: badge.description,
      earnedDate: formatDateKey(new Date(badge.achievedAt))
    }));
  }

  return {
    achievedBadges,
    setBadgeSectionData
  }
});
export default useBadgeSection;