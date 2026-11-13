package com.ssafy.yumyum.domain.badge.repository;

import com.ssafy.yumyum.domain.badge.repository.badgeCategory.BadgeCategoryMapper;
import com.ssafy.yumyum.domain.badge.repository.badgeCategory.BadgeCategoryVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BadgeCategoryMapperTest {

    @Autowired
    private BadgeCategoryMapper badgeCategoryMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = badgeCategoryMapper.count();
        System.out.println("총 개수: " + count);

        List<BadgeCategoryVO> list = badgeCategoryMapper.findAll();
        for (BadgeCategoryVO category : list) {
            System.out.println(category);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<BadgeCategoryVO> category = badgeCategoryMapper.findById(1L);
        System.out.println(category.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        BadgeCategoryVO category = BadgeCategoryVO.builder()
                .badgeCategoryName("새로운 카테고리")
                .build();

        badgeCategoryMapper.insert(category);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = badgeCategoryMapper.maxId();

        BadgeCategoryVO category = BadgeCategoryVO.builder()
                .badgeCategoryId(maxId)
                .badgeCategoryName("수정된 카테고리")
                .build();

        badgeCategoryMapper.updateById(category);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = badgeCategoryMapper.maxId();
        badgeCategoryMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
