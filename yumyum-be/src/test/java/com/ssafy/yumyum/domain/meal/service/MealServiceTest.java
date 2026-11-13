package com.ssafy.yumyum.domain.meal.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.meal.dto.request.AddMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.AddMealRequest.FoodRequest;
import com.ssafy.yumyum.domain.meal.dto.request.ApplyRecommendMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.TotalFoodRequest;
import com.ssafy.yumyum.domain.meal.dto.request.UpdateMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.UpdateMealRequest.UpdateFood;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse.IntakeValue;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealDetailResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealDetailResponse.GetMealDetailFood;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse.GetMealOfDateMeal;
import com.ssafy.yumyum.domain.meal.dto.response.GetRecommendMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetWeeklyDailyNutrionIntakeRateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.SearchFoodResponse;
import com.ssafy.yumyum.domain.meal.dto.response.TotalFoodResponse;
import com.ssafy.yumyum.domain.meal.service.dto.FoodAmountDTO;
import com.ssafy.yumyum.domain.meal.service.dto.GetMealOfDateFoodDTO;

@SpringBootTest
public class MealServiceTest {
	@Autowired
	private MealService mealService;
	
	@Test
	void getMealOfDate() {
		Long userId = 11L;
		LocalDate localDate = LocalDate.of(2025, 12, 3);
		GetMealOfDateResponse getMealOfDateResponse = 
				mealService.getMealOfDate(userId, localDate);
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("mealTotalCalorie: %f\n", 
				getMealOfDateResponse.getMealTotalCalorie()));
		buffer.append(String.format("mealTotalPoint: %d\n", 
				getMealOfDateResponse.getMealTotalPoint()));
		buffer.append(String.format("mealTotalBenefitPoint: %\n", 
				getMealOfDateResponse.getMealTotalBenefitPoint()));
		List<LocalDate> recordedDates = getMealOfDateResponse.getRecordedDates();
		buffer.append("recordedDates: [\n");
		for (LocalDate recordedDate : recordedDates)
			buffer.append(String.format("\t%s,\n", recordedDate.toString()));
		buffer.append("]");
		
		List<GetMealOfDateMeal> meals = getMealOfDateResponse.getMeals();
		buffer.append("meals: [\n");
		for (GetMealOfDateMeal meal : meals) {
			buffer.append("{\n");
			buffer.append(String.format("\tmealId: %d\n", meal.getMealId()));
			buffer.append(String.format("\tmealTypeName: %s\n", meal.getMealTypeName()));
			buffer.append(String.format("\tmealCreatorName: %s\n", meal.getMealCreatorName()));
			buffer.append(String.format("\ttotalPoint: %d\n", meal.getTotalPoint()));
			buffer.append(String.format("\tbenefitPoint: %d\n", meal.getBenefitPoint()));
			buffer.append(String.format("\ttotalCalorie: %f\n", meal.getTotalCalorie()));
			buffer.append(String.format("\ttotalCarbohydrate: %f\n", meal.getTotalCarbohydrate()));
			buffer.append(String.format("\ttotalProtein: %f\n", meal.getTotalProtein()));
			buffer.append(String.format("\ttotalFat: %f\n", meal.getTotalFat()));
			List<GetMealOfDateFoodDTO> foods = meal.getFoods();
			buffer.append("\tfoods: [\n");
			for (GetMealOfDateFoodDTO food : foods) {
				buffer.append("\t{\n");
				buffer.append(String.format("\t\tfoodId: %d\n", food.getFoodId()));
				buffer.append(String.format("\t\tname: %s\n", food.getName()));
				buffer.append(String.format("\t\tunitName: %s\n", food.getUnitName()));
				buffer.append(String.format("\t\tamount: %f\n", food.getAmount()));
				buffer.append("\t}\n");
			}
			buffer.append("\t]\n");
			buffer.append("}\n");
		}
		buffer.append("]");
		System.out.println(buffer);
	}
	@Test
	void addMeal() {
		Long userId = 5L;
		List<FoodRequest> foods = new ArrayList<>();
		foods.add(FoodRequest.builder()
				.foodId(55L)
				.amount(110.0)
				.build());
		foods.add(FoodRequest.builder()
				.foodId(20L)
				.amount(180.0)
				.build());
		foods.add(FoodRequest.builder()
				.foodId(13L)
				.amount(120.0)
				.build());
		foods.add(FoodRequest.builder()
				.foodId(2L)
				.amount(100.0)
				.build());
		foods.add(FoodRequest.builder()
				.foodId(5L)
				.amount(90.0)
				.build());
		AddMealRequest addMealRequest = AddMealRequest.builder()
				.mealDate(LocalDate.of(1999, 7, 13))
				.mealTypeName("점심")
				.mealCreatorName("USER")
				.totalPoint(18)
				.totalCalorie(550.0)
				.foods(foods)
				.build();
		mealService.addMeal(userId, addMealRequest);
	}
	@Test
	void getMealDetail() {
		Long mealId = 8L;
		GetMealDetailResponse getMealDetailResponse = 
				mealService.getMealDetail(mealId);
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("mealId: %d\n", 
				getMealDetailResponse.getMealId()));
		buffer.append(String.format("mealDate: %s\n", 
				getMealDetailResponse.getMealDate().toString()));
		buffer.append(String.format("mealTypeName: %s\n", 
				getMealDetailResponse.getMealTypeName()));
		buffer.append(String.format("mealCreatorName: %s\n", 
				getMealDetailResponse.getMealCreatorName()));
		buffer.append(String.format("totalPoint: %d\n", 
				getMealDetailResponse.getTotalPoint()));
		buffer.append(String.format("benefitPoint: %d\n", 
				getMealDetailResponse.getBenefitPoint()));
		List<GetMealDetailFood> foods = 
				getMealDetailResponse.getFoods();
		buffer.append("foods: [\n");
		for (GetMealDetailFood food : foods) {
			buffer.append("{\n");
			buffer.append(String.format("\tfoodId: %d\n", food.getFoodId()));
			buffer.append(String.format("\tname: %s\n", food.getName()));
			buffer.append(String.format("\tunitName: %s\n", food.getUnitName()));
			buffer.append(String.format("\tamount: %f\n", food.getAmount()));
			buffer.append(String.format("\tcalorie: %f\n", food.getCalorie()));
			buffer.append(String.format("\tcarbohydrate %f\n", food.getCarbohydrate()));
			buffer.append(String.format("\tprotein: %f\n", food.getProtein()));
			buffer.append(String.format("\tfat: %f\n", food.getFat()));
			buffer.append("}\n");
		}
		buffer.append("]\n");
		System.out.println(buffer);
	}
	@Test
	void updateMeal() {
		List<UpdateFood> updateFoods = new ArrayList<>();
		updateFoods.add(UpdateFood.builder()
				.foodId(55L)
				.updateType("REMOVE")
				.amount(null)
				.build());
		updateFoods.add(UpdateFood.builder()
				.foodId(2L)
				.updateType("REMOVE")
				.amount(null)
				.build());
		updateFoods.add(UpdateFood.builder()
				.foodId(10L)
				.updateType("ADD")
				.amount(145.0)
				.build());
		updateFoods.add(UpdateFood.builder()
				.foodId(13L)
				.updateType("MODIFY")
				.amount(168.0)
				.build());
		updateFoods.add(UpdateFood.builder()
				.foodId(7L)
				.updateType("ADD")
				.amount(172.0)
				.build());
		mealService.updateMeal(UpdateMealRequest.builder()
					.mealId(7L)
					.totalPoint(13)
					.totalCalorie(350.0)
					.updateFoods(updateFoods)
					.build());
	}
	@Test
	void deleteMeal() {
		mealService.deleteMeal(9L);
	}
	@Test
	void applyRecommendMeal() {
		List<UpdateFood> updateFoods = new ArrayList<>();
		updateFoods.add(UpdateFood.builder()
				.foodId(20L)
				.updateType("MODIFY")
				.amount(125.0)
				.build());
		updateFoods.add(UpdateFood.builder()
				.foodId(5L)
				.updateType("MODIFY")
				.amount(110.0)
				.build());
		updateFoods.add(UpdateFood.builder()
				.foodId(2L)
				.updateType("MODIFY")
				.amount(180.0)
				.build());
		mealService.applyRecommendMeal(ApplyRecommendMealRequest.builder()
					.mealId(8L)
					.mealTypeName("점심")
					.totalPoint(13)
					.totalCalorie(250.0)
					.updateFoods(updateFoods)
					.build());
	}
	@Test
	void searchFood() {
		String name = "국밥";
		List<SearchFoodResponse> searchFoodResponses = mealService.searchFood(name);
		searchFoodResponses.forEach(response -> {
			StringBuffer buffer = new StringBuffer("==========================\n");
			buffer.append(String.format("foodId: %d\n", response.getFoodId()))
					.append(String.format("name: %s\n", response.getName()))
					.append(String.format("unitName: %s\n", response.getUnitName()))
					.append(String.format("amount: %f\n", response.getAmount()))
					.append(String.format("calorie: %f\n", response.getCalorie()))
					.append(String.format("carbohydrate: %f\n", response.getCarbohydrate()))
					.append(String.format("protein: %f\n", response.getProtein()))
					.append(String.format("fat: %f\n", response.getFat()));
			System.out.println(buffer);
		});
	}
	@Test
	void totalFood() {
		Long userId = 11L;
		Boolean isSnack = false;
		List<FoodAmountDTO> foods = new ArrayList<>();
		foods.add(FoodAmountDTO.builder().foodId(10L).amount(200.0).build());
		foods.add(FoodAmountDTO.builder().foodId(9L).amount(130.0).build());
		TotalFoodRequest totalFoodRequest = TotalFoodRequest.builder()
				.isSnack(isSnack).foods(foods).build();
		TotalFoodResponse totalFoodResponse = mealService.totalFood(userId, totalFoodRequest);
		StringBuffer buffer = new StringBuffer("==========================\n");
		buffer.append(String.format("totalPoint: %d\n", totalFoodResponse.getTotalPoint()))
				.append(String.format("totalCalorie: %f\n", totalFoodResponse.getTotalCalorie()))
				.append(String.format("totalCarbohydrate: %f\n", totalFoodResponse.getTotalCarbohydrate()))
				.append(String.format("totalProtein: %f\n", totalFoodResponse.getTotalProtein()))
				.append(String.format("totalFat: %f", totalFoodResponse.getTotalFat()));
		System.out.println(buffer);
	}
	@Test
	void getMonthRecord() {
		List<LocalDate> getMonthRecordResponse = 
				mealService.getMonthRecord(11L, 2025, 12);
		getMonthRecordResponse.forEach(System.out::println);
	}
	@Test
	void getRecommendMeal() {
	    Long userId = 1L;
	    LocalDate mealDate = LocalDate.of(2025, 12, 5);

	    List<GetRecommendMealResponse> resList =
	            mealService.getRecommendMeal(userId, mealDate);

	    StringBuffer buffer = new StringBuffer("============================\n");
	    buffer.append("[\n");

	    resList.forEach(res -> {
	        buffer.append("\t{\n");
	        buffer.append(String.format("\t\tmealId: %d\n", res.getMealId()));
	        buffer.append(String.format("\t\tmealCreatorName: %s\n", res.getMealCreatorName()));
	        buffer.append(String.format("\t\ttitle: %s\n", res.getTitle()));
	        buffer.append(String.format("\t\tdescription: %s\n", res.getDescription()));

	        buffer.append(String.format("\t\ttotalCalorie: %f\n", res.getTotalCalorie()));
	        buffer.append(String.format("\t\ttotalCarbohydrate: %f\n", res.getTotalCarbohydrate()));
	        buffer.append(String.format("\t\ttotalProtein: %f\n", res.getTotalProtein()));
	        buffer.append(String.format("\t\ttotalFat: %f\n", res.getTotalFat()));

	        // foods
	        buffer.append("\t\tfoods: [\n");
	        res.getFoods().forEach(food -> {
	            buffer.append("\t\t\t{\n");
	            buffer.append(String.format("\t\t\t\tfoodId: %d\n", food.getFoodId()));
	            buffer.append(String.format("\t\t\t\tname: %s\n", food.getName()));
	            buffer.append(String.format("\t\t\t\tunitName: %s\n", food.getUnitName()));
	            buffer.append(String.format("\t\t\t\tamount: %f\n", food.getAmount()));
	            buffer.append("\t\t\t}\n");
	        });
	        buffer.append("\t\t]\n");

	        buffer.append("\t}\n");
	    });

	    buffer.append("]");

	    System.out.println(buffer);
	}


	@Test
	void getIntakeOfDate() {
	    Long userId = 13L;
	    LocalDate date = LocalDate.now();

	    GetIntakeOfDateResponse getIntakeOfDateResponse =
	            mealService.getIntakeOfDate(userId, date);

	    StringBuffer buffer = new StringBuffer("============================\n");
	    // calorie
	    buffer.append("calorie: {\n");
	    IntakeValue calorie = getIntakeOfDateResponse.getCalorie();
	    buffer.append(String.format("\tintake: %f\n", calorie.getIntake()));
	    buffer.append(String.format("\ttarget: %f\n", calorie.getTarget()));
	    buffer.append(String.format("\trate: %f\n", calorie.getRate()));
	    buffer.append("}\n");

	    // carbohydrate
	    buffer.append("carbohydrate: {\n");
	    IntakeValue carbohydrate = getIntakeOfDateResponse.getCarbohydrate();
	    buffer.append(String.format("\tintake: %f\n", carbohydrate.getIntake()));
	    buffer.append(String.format("\ttarget: %f\n", carbohydrate.getTarget()));
	    buffer.append(String.format("\trate: %f\n", carbohydrate.getRate()));
	    buffer.append("}\n");

	    // protein
	    buffer.append("protein: {\n");
	    IntakeValue protein = getIntakeOfDateResponse.getProtein();
	    buffer.append(String.format("\tintake: %f\n", protein.getIntake()));
	    buffer.append(String.format("\ttarget: %f\n", protein.getTarget()));
	    buffer.append(String.format("\trate: %f\n", protein.getRate()));
	    buffer.append("}\n");

	    // fat 
	    buffer.append("fat: {\n");
	    IntakeValue fat = getIntakeOfDateResponse.getFat();
	    buffer.append(String.format("\tintake: %f\n", fat.getIntake()));
	    buffer.append(String.format("\ttarget: %f\n", fat.getTarget()));
	    buffer.append(String.format("\trate: %f\n", fat.getRate()));
	    buffer.append("}\n");
	    
	    // fiber 
	    buffer.append("fiber: {\n");
	    IntakeValue fiber = getIntakeOfDateResponse.getFiber();
	    buffer.append(String.format("\tintake: %f\n", fiber.getIntake()));
	    buffer.append(String.format("\ttarget: %f\n", fiber.getTarget()));
	    buffer.append(String.format("\trate: %f\n", fiber.getRate()));
	    buffer.append("}\n");
	    
	    // calcium
	    buffer.append("calcium: {\n");
	    IntakeValue calcium = getIntakeOfDateResponse.getCalcium();
	    buffer.append(String.format("\tintake: %f\n", calcium.getIntake()));
	    buffer.append(String.format("\ttarget: %f\n", calcium.getTarget()));
	    buffer.append(String.format("\trate: %f\n", calcium.getRate()));
	    buffer.append("}");

	    System.out.println(buffer);
	}

	@Test
	void getWeeklyDailyNutrionIntakeRate() {
	    Long userId = 5L;

	    GetWeeklyDailyNutrionIntakeRateResponse res =
	            mealService.getWeeklyDailyNutrionIntakeRate(userId);

	    StringBuffer buffer = new StringBuffer("============================\n");
	    buffer.append(String.format("userId: %d\n", userId));

	    buffer.append("weekly: {\n");
	    buffer.append(String.format("\ttotalIntakeCalorie: %f\n",
	            res.getWeekly().getTotalIntakeCalorie()));

	    buffer.append("\tcarbohydrate: {\n");
	    buffer.append(String.format("\t\tintakeCalorie: %f\n",
	            res.getWeekly().getCarbohydrate().getIntakeCalorie()));
	    buffer.append(String.format("\t\trate: %f\n",
	            res.getWeekly().getCarbohydrate().getRate()));
	    buffer.append("\t}\n");

	    buffer.append("\tprotein: {\n");
	    buffer.append(String.format("\t\tintakeCalorie: %f\n",
	            res.getWeekly().getProtein().getIntakeCalorie()));
	    buffer.append(String.format("\t\trate: %f\n",
	            res.getWeekly().getProtein().getRate()));
	    buffer.append("\t}\n");

	    buffer.append("\tfat: {\n");
	    buffer.append(String.format("\t\tintakeCalorie: %f\n",
	            res.getWeekly().getFat().getIntakeCalorie()));
	    buffer.append(String.format("\t\trate: %f\n",
	            res.getWeekly().getFat().getRate()));
	    buffer.append("\t}\n");
	    buffer.append("}\n");

	    buffer.append("daily: {\n");
	    buffer.append(String.format("\ttotalIntakeCalorie: %f\n",
	            res.getDaily().getTotalIntakeCalorie()));

	    buffer.append("\tcarbohydrate: {\n");
	    buffer.append(String.format("\t\tintakeCalorie: %f\n",
	            res.getDaily().getCarbohydrate().getIntakeCalorie()));
	    buffer.append(String.format("\t\trate: %f\n",
	            res.getDaily().getCarbohydrate().getRate()));
	    buffer.append("\t}\n");

	    buffer.append("\tprotein: {\n");
	    buffer.append(String.format("\t\tintakeCalorie: %f\n",
	            res.getDaily().getProtein().getIntakeCalorie()));
	    buffer.append(String.format("\t\trate: %f\n",
	            res.getDaily().getProtein().getRate()));
	    buffer.append("\t}\n");

	    buffer.append("\tfat: {\n");
	    buffer.append(String.format("\t\tintakeCalorie: %f\n",
	            res.getDaily().getFat().getIntakeCalorie()));
	    buffer.append(String.format("\t\trate: %f\n",
	            res.getDaily().getFat().getRate()));
	    buffer.append("\t}\n");
	    buffer.append("}");

	    System.out.println(buffer);
	}

}
