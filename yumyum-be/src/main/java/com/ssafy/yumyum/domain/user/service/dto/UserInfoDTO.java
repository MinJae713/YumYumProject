package com.ssafy.yumyum.domain.user.service.dto;

import com.ssafy.yumyum.domain.user.types.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private Long userId;
    private String email;
    private String name;

    private Gender gender;	//초기 리서치
    private LocalDate birthDate; //초기리서치
    private Double height;	//초기리서치
    private Double currentWeight; 	//초기리서치
    private Double targetWeight;
    private String goalType; 	//초기리서치
    private Integer mealsPerDay; 	//초기리서치
    private Integer point;
    private LocalTime remindTime; 	//초기리서치
    private Long userRoleId;
    private String userRoleName;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
