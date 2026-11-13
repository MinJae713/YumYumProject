package com.ssafy.yumyum.domain.user.dto.request;

import com.ssafy.yumyum.domain.user.types.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResearchRequest {
    private Gender gender;
    private LocalDate birthDate;
    private Double height;
    private Double currentWeight;
    private Double targetWeight;
    private String goalType;
    private Integer mealsPerDay;
    private LocalTime remindTime;
}
