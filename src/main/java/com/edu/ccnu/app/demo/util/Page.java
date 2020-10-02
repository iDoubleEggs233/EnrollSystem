package com.edu.ccnu.app.demo.util;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/22/18:50
 * @Description: 分页所用工具类
 */
public class Page {

    public final static int PAGESIZE = 10;
    public final static int PAGENUM = 1;

    // 记录总数
    private int total;
    // 每页的最大记录数
    private int pageSize = PAGESIZE;
    // 总页数
    private int totalPage;
    // 开始的记录数
    private int startPage;
    // 结束的记录数
    private int endItems;
    // 当前的页码
    private int pageNum = PAGENUM;

    public static int getPAGESIZE() {
        return PAGESIZE;
    }

    public static int getPAGENUM() {
        return PAGENUM;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndItems() {
        return endItems;
    }

    public void setEndItems(int endItems) {
        this.endItems = endItems;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
