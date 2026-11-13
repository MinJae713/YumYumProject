export const getNutrionLabel = (key) => {
  switch (key) {
    case 'calorie':
      return "칼로리";
    case 'carbohydrate':
      return "탄수화물";
    case 'protein':
      return "단백질";
    case 'fat':
      return "지방";
    case 'fiber':
      return "무기염류";
    case 'calcium':
      return "칼슘";
  }
}

export const getRecommendLabel = (mealCreaterType) => {
  switch (mealCreaterType) {
    case 'AI':
      return "AI 추천";
    case 'GROUP':
      return "그룹장 추천";
  }
}