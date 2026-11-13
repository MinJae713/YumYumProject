package com.ssafy.yumyum.domain.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.user.dto.response.GetWeightDeltaResponse;

@SpringBootTest
public class GetWeightDeltaTest {
	// 기존 UserServiceImplTest에서 실행이 안되는 관계로 임시로 생성

	@Autowired
	private UserService userService;
	
	@Test
    void getWeightDelta() {
        Long userId = 13L;

        GetWeightDeltaResponse res =
                userService.getWeightDelta(userId);

        StringBuffer buffer = new StringBuffer("============================\n");
        buffer.append(String.format("userId: %d\n", userId));

        buffer.append(String.format("startWeight: %f\n", res.getStartWeight()));
        buffer.append(String.format("currentWeight: %f\n", res.getCurrentWeight()));
        buffer.append(String.format("lossWeight: %f\n", res.getLossWeight()));
        buffer.append(String.format("targetWeight: %f\n", res.getTargetWeight()));

        buffer.append("dateOfWeight: [\n");
        res.getDateOfWeight().forEach(record -> {
            buffer.append("\t{\n");
            buffer.append(String.format("\t\trecordedDate: %s\n", record.getRecordedDate()));
            buffer.append(String.format("\t\tweight: %f\n", record.getWeight()));
            buffer.append("\t}\n");
        });
        buffer.append("]");

        System.out.println(buffer);
    }
}
