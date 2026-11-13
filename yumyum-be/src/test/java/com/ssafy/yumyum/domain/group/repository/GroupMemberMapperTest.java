package com.ssafy.yumyum.domain.group.repository;

import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberMapper;
import com.ssafy.yumyum.domain.group.repository.groupMember.GroupMemberVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class GroupMemberMapperTest {

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = groupMemberMapper.count();
        System.out.println("총 개수: " + count);

        List<GroupMemberVO> list = groupMemberMapper.findAll();
        for (GroupMemberVO member : list) {
            System.out.println(member);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<GroupMemberVO> member = groupMemberMapper.findById(1L);
        System.out.println(member.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        GroupMemberVO member = GroupMemberVO.builder()
                .role("member")
                .isActive(true)
                .joinedAt(LocalDateTime.now())
                .points(10)
                .userId(1L)
                .groupId(1L)
                .build();

        groupMemberMapper.insert(member);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = groupMemberMapper.maxId();

        GroupMemberVO member = GroupMemberVO.builder()
                .groupMemberId(maxId)
                .role("leader")
                .isActive(false)
                .joinedAt(LocalDateTime.now())
                .points(20)
                .userId(2L)
                .groupId(2L)
                .build();

        groupMemberMapper.updateById(member);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = groupMemberMapper.maxId();
        groupMemberMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
