package org.cp4j.core;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {

    private int total;
    private int pageSize;
    private int currentPage;
    private List<T> list;
    private String serverTime;

}
