package com.ssafy.yumyum.domain.user.repository;

import com.ssafy.yumyum.domain.user.repository.userRole.UserRoleMapper;
import com.ssafy.yumyum.domain.user.repository.userRole.UserRoleVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserRoleMapperTest {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = userRoleMapper.count();
        System.out.println("총 개수: " + count);

        List<UserRoleVO> list = userRoleMapper.findAll();
        for (UserRoleVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<UserRoleVO> vo = userRoleMapper.findById(1L);
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        UserRoleVO vo = UserRoleVO.builder()
                .userRoleName("새로운 역할")
                .build();

        userRoleMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = userRoleMapper.maxId();

        UserRoleVO vo = UserRoleVO.builder()
                .userRoleId(maxId)
                .userRoleName("업데이트 역할")
                .build();

        userRoleMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = userRoleMapper.maxId();
        userRoleMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
