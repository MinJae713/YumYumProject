package com.ssafy.yumyum.domain.badge.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.badge.repository.badgeProgress.BadgeProgressMapper;
import com.ssafy.yumyum.domain.badge.repository.badgeProgress.BadgeProgressVO;
import com.ssafy.yumyum.domain.badge.types.StreakType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BadgeProgressMapperTest {

    @Autowired
    private BadgeProgressMapper badgeProgressMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = badgeProgressMapper.count();
        System.out.println("총 개수: " + count);

        List<BadgeProgressVO> list = badgeProgressMapper.findAll();
        for (BadgeProgressVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<BadgeProgressVO> vo = 
        		badgeProgressMapper.findById(badgeProgressMapper.maxId());
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
    	LocalDate startDate = LocalDate.of(1999, 7, 13);
    	LocalDate lastDate = LocalDate.of(2019, 11, 4);
    	Long longestStreak = ChronoUnit.DAYS.between(startDate, lastDate);
        BadgeProgressVO vo = BadgeProgressVO.builder()
        		.streakType(StreakType.EXERCISE_RECORD)
        		.startDate(startDate)
                .longestStreak(longestStreak)
                .lastDate(lastDate)
                .userId(1L)
                .build();

        badgeProgressMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = badgeProgressMapper.maxId();
        LocalDate startDate = LocalDate.of(2021, 5, 21);
    	LocalDate lastDate = LocalDate.now();
    	Long longestStreak = ChronoUnit.DAYS.between(startDate, lastDate);
        BadgeProgressVO vo = BadgeProgressVO.builder()
                .badgeProgressId(maxId)
                .streakType(StreakType.MEAL_RECORD)
                .startDate(startDate)
                .longestStreak(longestStreak)
                .lastDate(lastDate)
                .userId(1L)
                .build();

        badgeProgressMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = badgeProgressMapper.maxId();
        badgeProgressMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
