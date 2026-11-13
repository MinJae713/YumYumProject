package com.ssafy.yumyum.common.contextListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.ssafy.yumyum.domain.exercise.repository.exerciseCatalog.ExerciseCatalogMapper;
import com.ssafy.yumyum.domain.exercise.repository.exerciseCatalog.ExerciseCatalogVO;
import com.ssafy.yumyum.domain.exercise.repository.exerciseCategory.ExerciseCategoryMapper;
import com.ssafy.yumyum.domain.exercise.repository.exerciseCategory.ExerciseCategoryVO;

@Component
public class ExerciseInitializer {
	private ExerciseCatalogMapper exerciseCatalogMapper;
	private ExerciseCategoryMapper exerciseCategoryMapper;
	private ResourceLoader resourceLoader;
	private String csvPath;
	
	public ExerciseInitializer(ExerciseCatalogMapper exerciseCatalogMapper,
			ExerciseCategoryMapper exerciseCategoryMapper,
			ResourceLoader resourceLoader,
			@Value("${exerciseApi.csvPath}") String csvPath,
			@Value("${exerciseApi.loadAble}") boolean loadAble) {
		this.exerciseCatalogMapper = exerciseCatalogMapper;
		this.exerciseCategoryMapper = exerciseCategoryMapper;
		this.resourceLoader = resourceLoader;
		this.csvPath = csvPath;
		if (loadAble && exerciseCatalogMapper.count() == 0)
			loadExerciseData();
	}

	private void loadExerciseData() { 
		// Global Exception 처리는 누나꺼 PR 후 만들 것!
		Resource csv = resourceLoader.getResource(csvPath);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(csv.getInputStream()));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				ExerciseCatalogVO exerciseCatalogVO = getExerciseCatalogVO(line);
				if (exerciseCatalogVO == null) continue;
				System.out.println(line);
				exerciseCatalogMapper.insert(exerciseCatalogVO);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.printf("데이터 수 - %d\n", exerciseCatalogMapper.count());
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private ExerciseCatalogVO getExerciseCatalogVO(String line) {
		String[] lineArr = line.split(",");
		String name = lineArr[0] == null ? "" : lineArr[0];
		if (exerciseCatalogMapper.findByName(name).isPresent())
			return null;
		Double metValue = lineArr[1] == null ? 
				0 : Double.parseDouble(lineArr[1]);
		String categoryName = lineArr[2];
		return ExerciseCatalogVO.builder().name(name).metValue(metValue)
				.exerciseCategoryId(getCategoryId(categoryName)).build();
	}

	private Long getCategoryId(String categoryName) {
		Long categoryId = exerciseCategoryMapper
				.findIdByCategoryName(categoryName).orElse(null);
		if (categoryId == null) {
			exerciseCategoryMapper.insert(ExerciseCategoryVO.builder()
					.exerciseCategoryName(categoryName).build());
			return exerciseCategoryMapper.maxId();
		} else return categoryId;
	}
}
