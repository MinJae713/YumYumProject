package com.ssafy.yumyum.common.util;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ParsedAmount {
	private Double number;
	private String unit;
	
	public ParsedAmount(String input) {
		number = Double.parseDouble(input.replaceAll("[^0-9.]", ""));
		unit = input.replaceAll("[0-9.]", "").replaceAll(",", "");
	}
}
