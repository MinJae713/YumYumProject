package com.ssafy.yumyum.domain.user.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.yumyum.domain.user.types.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String name;
    private Double targetWeight;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime remindTime;
    private Integer mealsPerDay;
    
    // 추가 -> 회원 정보 수정 시 수정할 정보들
    private Gender gender;
    private LocalDate birthDate;
    private Double height;
    private Double currentWeight;
    private String goalType;
}
