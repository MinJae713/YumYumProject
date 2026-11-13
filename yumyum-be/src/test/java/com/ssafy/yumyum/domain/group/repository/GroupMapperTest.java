package com.ssafy.yumyum.domain.group.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.group.repository.group.GroupMapper;
import com.ssafy.yumyum.domain.group.repository.group.GroupVO;

@SpringBootTest
public class GroupMapperTest {

    @Autowired
    private GroupMapper groupMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = groupMapper.count();
        System.out.println("총 개수: " + count);

        List<GroupVO> list = groupMapper.findAll();
        for (GroupVO group : list) {
            System.out.println(group);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<GroupVO> group = groupMapper.findById(1L);
        System.out.println(group.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        GroupVO group = GroupVO.builder()
                .groupName("테스트 그룹")
                .userId(1L)
                .build();

        groupMapper.insert(group);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = groupMapper.maxId();

        GroupVO group = GroupVO.builder()
                .groupId(maxId)
                .groupName("수정된 테스트 그룹")
                .userId(2L)
                .build();

        groupMapper.updateById(group);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = groupMapper.maxId();
        groupMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
