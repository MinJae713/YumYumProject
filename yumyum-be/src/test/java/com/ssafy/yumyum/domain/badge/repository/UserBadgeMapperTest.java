package com.ssafy.yumyum.domain.badge.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.badge.repository.userBadge.UserBadgeMapper;
import com.ssafy.yumyum.domain.badge.repository.userBadge.UserBadgeVO;

@SpringBootTest
public class UserBadgeMapperTest {

    @Autowired
    private UserBadgeMapper userBadgeMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = userBadgeMapper.count();
        System.out.println("총 개수: " + count);

        List<UserBadgeVO> list = userBadgeMapper.findAll();
        for (UserBadgeVO badge : list) {
            System.out.println(badge);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<UserBadgeVO> badge = userBadgeMapper.findById(1L);
        System.out.println(badge.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        UserBadgeVO badge = UserBadgeVO.builder()
                .userId(1L)
                .badgeId(1L)
                .build();

        userBadgeMapper.insert(badge);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = userBadgeMapper.maxId();

        UserBadgeVO badge = UserBadgeVO.builder()
                .userBadgeId(maxId)
                .userId(2L)
                .badgeId(2L)
                .build();

        userBadgeMapper.updateById(badge);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = userBadgeMapper.maxId();
        userBadgeMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
