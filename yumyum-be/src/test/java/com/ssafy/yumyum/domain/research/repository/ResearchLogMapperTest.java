package com.ssafy.yumyum.domain.research.repository;

import com.ssafy.yumyum.domain.research.types.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ResearchLogMapperTest {

    @Autowired
    private ResearchLogMapper researchLogMapper;


    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = researchLogMapper.count();
        System.out.println("총 개수: " + count);

        List<ResearchLogVO> list = researchLogMapper.findAll();
        list.forEach(System.out::println);
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<ResearchLogVO> vo = researchLogMapper.findById(1L);
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        ResearchLogVO vo = ResearchLogVO.builder()
                .userId(1L)
                .researchDate(LocalDate.now())
                .dietPerformance(DietPerformance.NORMAL)
                .exerciseIntensity(ExerciseIntensity.NONE)
                .digestionState(DigestionState.COMFORTABLE)
                .tomorrowActivity(TomorrowActivity.NORMAL)
                .freeMeal(FreeMeal.NONE)
                .build();

        int result = researchLogMapper.insert(vo);
        System.out.println("Insert 실행 완료, result=" + result);
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = researchLogMapper.maxId();

        ResearchLogVO vo = ResearchLogVO.builder()
                .researchId(maxId)
                .userId(1L)
                .researchDate(LocalDate.now())
                .dietPerformance(DietPerformance.HIGH)
                .exerciseIntensity(ExerciseIntensity.HIGH)
                .digestionState(DigestionState.HUNGRY)
                .tomorrowActivity(TomorrowActivity.HIGH)
                .freeMeal(FreeMeal.DINNER)
                .build();

        int result = researchLogMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK=" + maxId + "), result=" + result);
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = researchLogMapper.maxId();
        int result = researchLogMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK=" + maxId + "), result=" + result);
    }

    @Test
    @DisplayName("특정 유저의 최근 7개 조회")
    public void findTop7() {
        List<ResearchLogVO> list =
                researchLogMapper.findTop7ByUserIdOrderByResearchDateDesc(1L);

        list.forEach(System.out::println);
    }

    @Test
    @DisplayName("특정날짜 리서치 조회 (unique 검사 용)")
    public void findByUserIdAndResearchDate() {
        Optional<ResearchLogVO> vo =
                researchLogMapper.findByUserIdAndResearchDate(1L, LocalDate.now());

        System.out.println("결과: " + vo.orElse(null));
    }
}
