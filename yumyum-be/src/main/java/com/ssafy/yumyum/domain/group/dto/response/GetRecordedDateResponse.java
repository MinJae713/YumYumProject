package com.ssafy.yumyum.domain.group.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetRecordedDateResponse {
	private List<LocalDate> mealDates;
	private List<LocalDate> exerciseDates;
}
