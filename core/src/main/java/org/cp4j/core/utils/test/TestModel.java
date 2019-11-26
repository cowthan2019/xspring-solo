package org.cp4j.core.utils.test;

import lombok.Data;

import java.util.Date;

@Data
public class TestModel {

    private int real_age = 999;
    private Date createTime = new Date();
    private String userName = "字符串测试";
    private String ignore = "字符串测试2";

    private TestModel2 data = new TestModel2();
}
