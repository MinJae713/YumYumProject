package com.ssafy.yumyum.domain.user.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.ssafy.yumyum.domain.user.types.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponse {
    private Long userId;
    private String email;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private Double height;
    private Double currentWeight;
    private Double targetWeight;
    private String goalType;
    private Integer mealsPerDay;
    private Integer point;
    private LocalTime remindTime;
    private Long providerId;
    private String providerName;
    private Long userRoleId;
    private String userRoleName;
}
