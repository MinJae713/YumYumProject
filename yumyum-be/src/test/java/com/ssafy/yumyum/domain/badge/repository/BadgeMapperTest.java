package com.ssafy.yumyum.domain.badge.repository;

import com.ssafy.yumyum.domain.badge.repository.badge.BadgeMapper;
import com.ssafy.yumyum.domain.badge.repository.badge.BadgeVO;
import com.ssafy.yumyum.domain.badge.types.BadgeType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BadgeMapperTest {

    @Autowired
    private BadgeMapper badgeMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = badgeMapper.count();
        System.out.println("총 개수: " + count);

        List<BadgeVO> list = badgeMapper.findAll();
        for (BadgeVO badge : list) {
            System.out.println(badge);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
    	Long maxId = badgeMapper.maxId();
        Optional<BadgeVO> badge = badgeMapper.findById(maxId);
        System.out.println(badge.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        BadgeVO badge = BadgeVO.builder()
        		.badgeType(BadgeType.SELECT_MEAL_GROUP_10)
                .badgeTitle("새로운 배지")
                .description("테스트 배지 설명")
                .imageUrl("대충 주소")
                .badgeCategoryId(1L)
                .build();

        badgeMapper.insert(badge);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = badgeMapper.maxId();

        BadgeVO badge = BadgeVO.builder()
                .badgeId(maxId)
                .badgeType(BadgeType.WELL_DONE)
                .badgeTitle("수정된 배지 제목")
                .description("수정된 배지 설명")
                .imageUrl("대충 바뀐 주소")
                .badgeCategoryId(2L)
                .build();

        badgeMapper.updateById(badge);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = badgeMapper.maxId();
        badgeMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
