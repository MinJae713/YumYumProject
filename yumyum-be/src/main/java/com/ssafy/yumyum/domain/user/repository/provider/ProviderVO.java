package com.ssafy.yumyum.domain.user.repository.provider;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderVO {
    private Long providerId;
    private String providerName;
}
