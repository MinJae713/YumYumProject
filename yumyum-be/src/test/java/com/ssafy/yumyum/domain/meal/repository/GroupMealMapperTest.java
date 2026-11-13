package com.ssafy.yumyum.domain.meal.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.meal.repository.groupMeal.GroupMealMapper;
import com.ssafy.yumyum.domain.meal.repository.groupMeal.GroupMealVO;

@SpringBootTest
public class GroupMealMapperTest {
	// 얘는 꼭 insert 부터 실행할 것!!
    @Autowired
    private GroupMealMapper groupMealMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = groupMealMapper.count();
        System.out.println("총 개수: " + count);

        List<GroupMealVO> list = groupMealMapper.findAll();
        for (GroupMealVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<GroupMealVO> vo = groupMealMapper.findById(1L);
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        GroupMealVO vo = GroupMealVO.builder()
                .mealId(1L)
                .groupMemberId(1L)
                .build();

        groupMealMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = groupMealMapper.maxId();

        GroupMealVO vo = GroupMealVO.builder()
                .mealId(maxId)   // PK 사용
                .groupMemberId(2L)
                .build();

        groupMealMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = groupMealMapper.maxId();
        groupMealMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
