export const parseToCategoryLabel = (badgeCategoryName) => {
  switch (badgeCategoryName) {
    case "연속 식단 기록":
      return 'streak';
    case "연속 운동 기록":
      return 'streak';
    case "도전 과제":
      return 'achievement';
    case "건강 목표 달성":
      return 'achievement';
    case "피드백 마스터":
      return 'achievement';
    case "그룹 활동":
      return 'social';
  }
}