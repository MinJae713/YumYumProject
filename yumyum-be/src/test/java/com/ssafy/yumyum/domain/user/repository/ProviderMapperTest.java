package com.ssafy.yumyum.domain.user.repository;

import com.ssafy.yumyum.domain.user.repository.provider.ProviderMapper;
import com.ssafy.yumyum.domain.user.repository.provider.ProviderVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProviderMapperTest {

    @Autowired
    private ProviderMapper providerMapper;

    @Test
    @DisplayName("findAll 실행")
    public void findAll() {
        int count = providerMapper.count();
        System.out.println("총 개수: " + count);

        List<ProviderVO> list = providerMapper.findAll();
        for (ProviderVO vo : list) {
            System.out.println(vo);
        }
    }

    @Test
    @DisplayName("findById 실행")
    public void findById() {
        Optional<ProviderVO> vo = providerMapper.findById(1L);
        System.out.println(vo.orElse(null));
    }

    @Test
    @DisplayName("insert 실행")
    public void insert() {
        ProviderVO vo = ProviderVO.builder()
                .providerName("새로운 제공자")
                .build();

        providerMapper.insert(vo);
        System.out.println("Insert 실행 완료");
    }

    @Test
    @DisplayName("updateById 실행")
    public void updateById() {
        Long maxId = providerMapper.maxId();

        ProviderVO vo = ProviderVO.builder()
                .providerId(maxId)
                .providerName("업데이트 제공자")
                .build();

        providerMapper.updateById(vo);
        System.out.println("Update 실행 완료 (PK = " + maxId + ")");
    }

    @Test
    @DisplayName("deleteById 실행")
    public void deleteById() {
        Long maxId = providerMapper.maxId();
        providerMapper.deleteById(maxId);
        System.out.println("Delete 실행 완료 (PK = " + maxId + ")");
    }
}
