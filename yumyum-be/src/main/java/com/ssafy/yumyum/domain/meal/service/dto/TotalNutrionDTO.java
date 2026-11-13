package com.ssafy.yumyum.domain.meal.service.dto;

import com.ssafy.yumyum.common.util.ParsedAmount;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TotalNutrionDTO {
	private Double totalCalorie = 0.0;
	private Double totalCarbohydrate = 0.0;
	private Double totalProtein = 0.0;
	private Double totalFat = 0.0;
	private Double totalSugar = 0.0;
	private Double totalFiber = 0.0;
	private Double totalCalcium = 0.0;
	private Double totalIron = 0.0;
	private Double totalSodium = 0.0;
	private Double totalMagnesium = 0.0;
	
	public void addTotalNutrion(FoodVO foodVO, Double amount) {
		// Food는 100g당 수치 -> 140이면 그 수치가 얼마일까? 그게 여기서 나와야 함
		// 원래는 단위당 영양소 수치, 단위의 수를 받았던 것은
		// FoodVO 및 해당 식품의 섭취량 (amount)를 받음
		String servingSize = foodVO.getServingSize();
		totalCalorie += getAmount(foodVO.getEnergy(), servingSize, amount);
		totalCarbohydrate += getAmount(foodVO.getCarbohydrate(), servingSize, amount);
		totalProtein += getAmount(foodVO.getProtein(), servingSize, amount);
		totalFat += getAmount(foodVO.getFat(), servingSize, amount);
		totalSugar += getAmount(foodVO.getSugar(), servingSize, amount);
		totalFiber += getAmount(foodVO.getFiber(), servingSize, amount);
		totalCalcium += getAmount(foodVO.getCalcium(), servingSize, amount);
		totalIron += getAmount(foodVO.getIron(), servingSize, amount);
		totalSodium += getAmount(foodVO.getSodium(), servingSize, amount);
		totalMagnesium += getAmount(foodVO.getMagnesium(), servingSize, amount);
	}
	
	private Double getAmount(Double nutrion, 
			String servingSize, Double amount) {
		ParsedAmount parsedAmount = new ParsedAmount(servingSize);
		Double number = parsedAmount.getNumber();
		return (nutrion/number)*amount;
	}
}
