package com.ssafy.yumyum.domain.group.repository;

import com.ssafy.yumyum.domain.group.repository.groupInvite.GroupInviteMapper;
import com.ssafy.yumyum.domain.group.repository.groupInvite.GroupInviteVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class GroupInviteMapperTest {

    @Autowired
    private GroupInviteMapper groupInviteMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = groupInviteMapper.count();
        System.out.println("총 개수: " + count);

        List<GroupInviteVO> list = groupInviteMapper.findAll();
        for (GroupInviteVO invite : list) {
            System.out.println(invite);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<GroupInviteVO> invite = groupInviteMapper.findById(1L);
        System.out.println(invite.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        GroupInviteVO invite = GroupInviteVO.builder()
                .status("sent")
                .sentAt(LocalDateTime.now())
                .respondedAt(null)
                .userId(1L)
                .groupId(1L)
                .build();

        groupInviteMapper.insert(invite);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = groupInviteMapper.maxId();

        GroupInviteVO invite = GroupInviteVO.builder()
                .groupInviteId(maxId)
                .status("responded")
                .sentAt(LocalDateTime.now())
                .respondedAt(LocalDateTime.now())
                .userId(2L)
                .groupId(2L)
                .build();

        groupInviteMapper.updateById(invite);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = groupInviteMapper.maxId();
        groupInviteMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
