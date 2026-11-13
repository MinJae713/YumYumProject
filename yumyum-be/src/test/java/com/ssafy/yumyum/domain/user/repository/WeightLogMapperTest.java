package com.ssafy.yumyum.domain.user.repository;

import com.ssafy.yumyum.domain.user.repository.weightLog.WeightLogMapper;
import com.ssafy.yumyum.domain.user.repository.weightLog.WeightLogVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class WeightLogMapperTest {

    @Autowired
    private WeightLogMapper weightLogMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = weightLogMapper.count();
        System.out.println("총 개수: " + count);

        List<WeightLogVO> list = weightLogMapper.findAll();
        for (WeightLogVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<WeightLogVO> vo = weightLogMapper.findById(1L);
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        WeightLogVO vo = WeightLogVO.builder()
                .logDate(LocalDate.now())
                .weightKg(70.5)
                .userId(1L)
                .build();

        weightLogMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = weightLogMapper.maxId();

        WeightLogVO vo = WeightLogVO.builder()
                .weightLogId(maxId)
                .logDate(LocalDate.now())
                .weightKg(72.0)
                .userId(1L)
                .build();

        weightLogMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = weightLogMapper.maxId();
        weightLogMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
