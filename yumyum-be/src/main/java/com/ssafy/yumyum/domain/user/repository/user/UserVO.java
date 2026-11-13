package com.ssafy.yumyum.domain.user.repository.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.ssafy.yumyum.domain.user.types.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
	private Long userId;
	private String email;
	private String name;
	private String password;
	private Boolean isActive;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Gender gender;	//초기 리서치
	private LocalDate birthDate; //초기리서치
	private Double height;	//초기리서치
	private Double currentWeight; 	//초기리서치
	private Double targetWeight;	//초기리서치
	private String goalType; 	//초기리서치
	private Integer mealsPerDay; 	//초기리서치
	private Integer point;
	private String timezone;
	private LocalTime remindTime; 	//초기리서치
	private Long providerId;
	private Long userRoleId;

	private String userRoleName;
	private String providerName;
}
