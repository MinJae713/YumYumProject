package com.ssafy.yumyum.domain.notification.repository;

import com.ssafy.yumyum.domain.notification.repository.notificationType.NotificationTypeMapper;
import com.ssafy.yumyum.domain.notification.repository.notificationType.NotificationTypeVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class NotificationTypeMapperTest {

    @Autowired
    private NotificationTypeMapper notificationTypeMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = notificationTypeMapper.count();
        System.out.println("총 개수: " + count);

        List<NotificationTypeVO> list = notificationTypeMapper.findAll();
        for (NotificationTypeVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<NotificationTypeVO> vo = notificationTypeMapper.findById(1L);
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        NotificationTypeVO vo = NotificationTypeVO.builder()
                .notificationName("ALERT") 
                .build();

        notificationTypeMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = notificationTypeMapper.maxId();

        NotificationTypeVO vo = NotificationTypeVO.builder()
                .notificationTypeId(maxId)
                .notificationName("REMINDER") 
                .build();

        notificationTypeMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = notificationTypeMapper.maxId();
        notificationTypeMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
