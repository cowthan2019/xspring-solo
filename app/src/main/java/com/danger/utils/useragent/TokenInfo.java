package com.danger.utils.useragent;

import lombok.Data;

@Data
public class TokenInfo {
    private String username;
    private int roleId;
    private boolean expired;
}
