package com.ssafy.yumyum.domain.user.repository;

import com.ssafy.yumyum.domain.user.repository.user.UserMapper;
import com.ssafy.yumyum.domain.user.repository.user.UserVO;
import com.ssafy.yumyum.domain.user.types.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = userMapper.count();
        System.out.println("총 개수: " + count);

        List<UserVO> list = userMapper.findAll();
        for (UserVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<UserVO> vo = userMapper.findById(1L);
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        UserVO vo = UserVO.builder()
                .email("test@example.com")
                .name("홍길동")
                .password("password123")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1996,12,21))
                .height(175.0)
                .currentWeight(70.0)
                .goalType("체중감량")
                .mealsPerDay(3)
                .point(0)
                .timezone("Asia/Seoul")
                .remindTime(LocalTime.of(8, 0))
                .providerId(1L)
                .userRoleId(1L)
                .build();

        userMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = userMapper.maxId();

        UserVO vo = UserVO.builder()
                .userId(maxId)
                .email("update@example.com")
                .name("홍길동 수정")
                .password("newpassword123")
                .isActive(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1996,12,21))
                .height(180.0)
                .currentWeight(75.0)
                .goalType("근육증가")
                .mealsPerDay(4)
                .point(1000)
                .timezone("Asia/Seoul")
                .remindTime(LocalTime.of(9, 0))
                .providerId(1L)
                .userRoleId(1L)
                .build();

        userMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = userMapper.maxId();
        userMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
