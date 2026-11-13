package com.ssafy.yumyum.domain.notification.repository;

import com.ssafy.yumyum.domain.notification.repository.notification.NotificationMapper;
import com.ssafy.yumyum.domain.notification.repository.notification.NotificationVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class NotificationMapperTest {

    @Autowired
    private NotificationMapper notificationMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = notificationMapper.count();
        System.out.println("총 개수: " + count);

        List<NotificationVO> list = notificationMapper.findAll();
        for (NotificationVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<NotificationVO> vo = notificationMapper.findById(1L);
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        NotificationVO vo = NotificationVO.builder()
                .isRead(false)
                .userId(1L)  
                .notificationTypeId(1L)
                .build();

        notificationMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = notificationMapper.maxId();

        NotificationVO vo = NotificationVO.builder()
                .notificationId(maxId)
                .isRead(true)
                .userId(1L)
                .notificationTypeId(1L)
                .build();

        notificationMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = notificationMapper.maxId();
        notificationMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
