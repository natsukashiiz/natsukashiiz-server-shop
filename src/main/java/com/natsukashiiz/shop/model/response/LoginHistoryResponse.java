package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.LoginHistory;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.LoginHistory}
 */
@Getter
@Setter
public class LoginHistoryResponse implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private String ip;
    private String userAgent;
    private String device;
    private String os;

    public static LoginHistoryResponse build(LoginHistory loginHistory) {
        LoginHistoryResponse response = new LoginHistoryResponse();
        response.setId(loginHistory.getId());
        response.setCreatedAt(loginHistory.getCreatedAt());
        response.setIp(loginHistory.getIp());
        response.setUserAgent(loginHistory.getUserAgent());
        response.setDevice(loginHistory.getDevice());
        response.setOs(loginHistory.getOs());
        return response;
    }

    public static List<LoginHistoryResponse> buildList(List<LoginHistory> loginHistories) {
        return loginHistories.stream()
                .map(LoginHistoryResponse::build)
                .collect(Collectors.toList());
    }
}