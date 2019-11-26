package com.danger.app.user.model;

import lombok.Data;

@Data
public class RegisteredVO {
    private int registered;

    public RegisteredVO(int registered) {
        this.registered = registered;
    }
}
