package com.danger.app.user.model;

import lombok.Data;

@Data
public class UserQueryMorm {

    private int page;
    private int count;
    private int role;
    private String sortKey;
    private String sortOrder;
    private String keyword;

}
