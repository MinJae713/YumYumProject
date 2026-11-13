package com.ssafy.yumyum.domain.meal.repository.groupMeal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupMealVO {
	private Long mealId;
	private Long groupMemberId;
}
