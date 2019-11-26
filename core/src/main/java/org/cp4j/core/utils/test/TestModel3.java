package org.cp4j.core.utils.test;

import lombok.Data;

import java.util.Date;

@Data
public class TestModel3 {

    private int real_age;
    private Date createTime;
    private String userName ;
    private String ignore;

    private TestModel2 data;
}
