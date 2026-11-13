package com.ssafy.yumyum.common.contextListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.yumyum.common.util.ParsedAmount;
import com.ssafy.yumyum.domain.meal.repository.food.FoodMapper;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO.FoodVOBuilder;
import com.ssafy.yumyum.domain.meal.repository.foodUnit.FoodUnitMapper;
import com.ssafy.yumyum.domain.meal.repository.foodUnit.FoodUnitVO;

@Component
public class FoodInitializer {
	
	private String apiKey;
	private int pageStart;
	private int numOfRows; // max 500
	private int lastDataIndex;
	private String type;

	private FoodMapper foodMapper;
	private FoodUnitMapper foodUnitMapper;
	
	public FoodInitializer(FoodMapper foodMapper,
			FoodUnitMapper foodUnitMapper,
			@Value("${foodApi.apiKey}") String apiKey, 
			@Value("${foodApi.pageStart}") int pageStart,
			@Value("${foodApi.numOfRows}") int numOfRows,
			@Value("${foodApi.pageEnd}") int pageEnd,
			@Value("${foodApi.type}") String type,
			@Value("${foodApi.loadAble}") boolean loadAble) throws IOException {
		this.apiKey = apiKey;
		this.pageStart = pageStart;
		this.numOfRows = numOfRows;
		this.lastDataIndex = pageEnd * numOfRows;
		this.type = type;
		this.foodMapper = foodMapper;
		this.foodUnitMapper = foodUnitMapper;
		if (loadAble && foodMapper.count() == 0) loadFoodData();
	}

	@SuppressWarnings("unchecked")
	void loadFoodData() throws IOException{
		int pageCount = pageStart;
		do {
			Map<String, Object> body = sendAPI(pageCount);
			if (lastDataIndex <= 0) lastDataIndex = (int)body.get("totalCount");
			List<Map<String, Object>> foods = (List<Map<String, Object>>)body.get("items");
			if (!foods.isEmpty()) {
				int insertedCount = foodMapper.count();
				foods.stream().filter(food -> filterMissingFood(food))
				.map(food -> parseToFoodVO(food))
				.filter(foodVO -> foodMapper.countByFoodName(foodVO.getFoodName()) == 0)
				.forEach(foodVO -> {
					System.out.println(foodVO);
					foodMapper.insert(foodVO);
				});
				System.out.printf("Food Data %d개 삽입 완료 [테이블 요소 갯수]: %d\n", 
						foodMapper.count()-insertedCount, foodMapper.count());
				System.out.printf("%d => %d vs %d\n", pageCount, pageCount*numOfRows, lastDataIndex);
			} else System.out.println("음식 조회 실패");
		} while ((pageCount++)*numOfRows < lastDataIndex);
	}

	private FoodVOBuilder foodVOBuilder;
	// food 필터링
	private boolean filterMissingFood(Map<String, Object> food) {
		String foodName = (String)food.get("FOOD_NM_KR");
		if (foodName == null || foodName.isEmpty()) return false;
		String energy = (String)food.get("AMT_NUM1");
		if (energy == null || energy.isEmpty()) return false;
		String protein = (String)food.get("AMT_NUM3");
		if (protein == null || protein.isEmpty()) return false;
		String carbohydrate = (String)food.get("AMT_NUM6");
		if (carbohydrate == null || carbohydrate.isEmpty()) return false;
		String fat = (String)food.get("AMT_NUM4");
		if (fat == null || fat.isEmpty()) return false;
		String amount = (String)food.get("Z10500");
		if (amount == null || amount.isEmpty()) return false;
		ParsedAmount parsedAmount = new ParsedAmount(amount);
		foodVOBuilder = FoodVO.builder()
				.foodName(foodName)
				.energy(Double.parseDouble(energy.replaceAll(",", "")))
				.protein(Double.parseDouble(protein.replaceAll(",", "")))
				.carbohydrate(Double.parseDouble(carbohydrate.replaceAll(",", "")))
				.fat(Double.parseDouble(fat.replaceAll(",", "")))
				.amount(parsedAmount.getNumber())
				.foodUnitId(getUnitId(parsedAmount.getUnit()));
		return true;
	}

	private Long getUnitId(String unitName) {
		Long foodUnitId = foodUnitMapper.findIdByUnitName(unitName).orElse(null);
		if (foodUnitId == null) {
			foodUnitMapper.insert(FoodUnitVO.builder().unitName(unitName).build());
			return foodUnitMapper.maxId();
		} else return foodUnitId;
	}

	// 필터링된 Food를 VO로 변환
	private FoodVO parseToFoodVO(Map<String, Object> food) {
		String servingSize = (String)food.get("SERVING_SIZE");
		String water = (String)food.get("AMT_NUM2");
		String sugar = (String)food.get("AMT_NUM7");
		String fiber = (String)food.get("AMT_NUM8");
		String calcium = (String)food.get("AMT_NUM9");
		String iron = (String)food.get("AMT_NUM10");
		String potassium = (String)food.get("AMT_NUM12");
		String sodium = (String)food.get("AMT_NUM13");
		String vitaminA = (String)food.get("AMT_NUM15");
		String vitaminB1 = (String)food.get("AMT_NUM18");
		String vitaminC = (String)food.get("AMT_NUM21");
		String vitaminD = (String)food.get("AMT_NUM22");
		String cholesterol = (String)food.get("AMT_NUM23");
		String saturatedFattyAcid = (String)food.get("AMT_NUM24");
		String unSaturatedFattyAcid = (String)food.get("AMT_NUM61");
		String caffeine = (String)food.get("AMT_NUM100");
		String magnesium = (String)food.get("AMT_NUM111");
		String aminoAcid = (String)food.get("AMT_NUM120");
		return foodVOBuilder
				.servingSize(servingSize == null || servingSize.isEmpty() ? "" : servingSize)
				.water(water == null || water.isEmpty() ? 
						0 : Double.parseDouble(water))
				.sugar(sugar == null || sugar.isEmpty() ? 
						0 : Double.parseDouble(sugar.replaceAll(",", "")))
				.fiber(fiber == null || fiber.isEmpty() ? 
						0 : Double.parseDouble(fiber.replaceAll(",", "")))
				.calcium(calcium == null || calcium.isEmpty() ? 
						0 : Double.parseDouble(calcium.replaceAll(",", "")))
				.iron(iron == null || iron.isEmpty() ? 
						0 : Double.parseDouble(iron.replaceAll(",", "")))
				.potassium(potassium == null || potassium.isEmpty() ? 
						0 : Double.parseDouble(potassium.replaceAll(",", "")))
				.sodium(sodium == null || sodium.isEmpty() ? 
						0 : Double.parseDouble(sodium.replaceAll(",", "")))
				.vitaminA(vitaminA == null || vitaminA.isEmpty() ? 
						0 : Double.parseDouble(vitaminA.replaceAll(",", "")))
				.vitaminB1(vitaminB1 == null || vitaminB1.isEmpty() ? 
						0 : Double.parseDouble(vitaminB1.replaceAll(",", "")))
				.vitaminC(vitaminC == null || vitaminC.isEmpty() ? 
						0 : Double.parseDouble(vitaminC.replaceAll(",", "")))
				.vitaminD(vitaminD == null || vitaminD.isEmpty() ? 
						0 : Double.parseDouble(vitaminD.replaceAll(",", "")))
				.cholesterol(cholesterol == null || cholesterol.isEmpty() ? 
						0 : Double.parseDouble(cholesterol.replaceAll(",", "")))
				.saturatedFattyAcid(saturatedFattyAcid == null || saturatedFattyAcid.isEmpty() ? 
						0 : Double.parseDouble(saturatedFattyAcid.replaceAll(",", "")))
				.unsaturatedFattyAcid(unSaturatedFattyAcid == null || unSaturatedFattyAcid.isEmpty() ? 
						0 : Double.parseDouble(unSaturatedFattyAcid.replaceAll(",", "")))
				.caffeine(caffeine == null || caffeine.isEmpty() ? 
						0 : Double.parseDouble(caffeine.replaceAll(",", "")))
				.magnesium(magnesium == null || magnesium.isEmpty() ? 
						0 : Double.parseDouble(magnesium.replaceAll(",", "")))
				.aminoAcid(aminoAcid == null || aminoAcid.isEmpty() ? 
						0 : Double.parseDouble(aminoAcid.replaceAll(",", "")))
				.build();
	}

	// 호출 준비
	private Map<String, Object> sendAPI(int pageNo) throws IOException {
		URL url = new URL(buildURL(pageNo).toString());
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		int code = conn.getResponseCode();
		System.out.println("Response code: "+code);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, Object>> result = null;
		if (200 <= code && code <= 300)
			result = mapper.readValue(conn.getInputStream(), new TypeReference<>() {});
		else return null;
		conn.disconnect();
		Map<String, Object> body = result.get("body");
		return body;
	}
	
	private StringBuilder buildURL(int pageNo) throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder("https://apis.data.go.kr/1471000/FoodNtrCpntDbInfo02/getFoodNtrCpntDbInq02");
		builder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8"))
				.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows+"", "UTF-8"))
				.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"))
				.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo+"", "UTF-8"));
//				.append("&" + URLEncoder.encode("FOOD_NM_KR", "UTF-8") + "=" + URLEncoder.encode("계란", "UTF-8"));
		return builder;
	}
}
