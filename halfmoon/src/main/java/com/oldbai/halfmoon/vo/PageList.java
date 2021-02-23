package com.oldbai.halfmoon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageList<T> implements Serializable {
    //做分页要多少数据
    //当前页码
    private int currentPage;
    //总数量
    private long totalCount;
    //每一页多少数量
    private long pageSize;
    //总页数 = 总的数量 / 每页数量
    private long totalPage;
    // 数据
    private List<T> contents;
    //是否第一页
    private boolean isFirst;
    //是否尾页
    private boolean isLast;

    public PageList(int currentPage, long totalCount, long pageSize) {
        this.currentPage = currentPage;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        if (this.totalCount % this.pageSize == 0) {
            this.totalPage = this.totalCount / this.pageSize;
        } else {
            this.totalPage = (this.totalCount / this.pageSize) + 1;
        }
        // 计算总的页数
        // 是否为第一页 / 是否为最后一页
        // 第一页为 0 ，最后一页为总的页码
        // 10 ， 每一页 有10 ==> 1
        // 100 ， 每一页 有10 ==> 10
        this.isFirst = this.currentPage == 1;
        this.isLast = this.currentPage == totalPage;
    }
}
