package com.ssafy.yumyum.domain.meal.repository.food;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoodVO {
    private Long foodId;
    private String foodName;

    private Double energy;
    private String servingSize;
    private Double water;
    private Double protein;
    private Double carbohydrate;
    private Double fat;
    private Double sugar;
    private Double fiber;
    private Double calcium;
    private Double iron;
    private Double potassium;
    private Double sodium;
    private Double vitaminA;
    private Double vitaminB1;
    private Double vitaminC;
    private Double vitaminD;
    private Double cholesterol;
    private Double saturatedFattyAcid;
    private Double unsaturatedFattyAcid;
    private Double caffeine;
    private Double magnesium;
    private Double aminoAcid;

    private Double amount;
    private Long foodUnitId;
}
