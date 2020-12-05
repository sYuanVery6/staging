package com.ayuan.staging.entity.vo;


import springfox.documentation.spring.web.json.Json;

/**
 * 接收前端请求的对象
 *
 * @author sYuan
 */
public class RequestEntity {

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 数据总条数
     */
    private Long totalSize;

    /**
     * 页码总数
     */
    private Long totalPage;

    /**
     * 数据模型
     */
    private Json data;


    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Json getData() {
        return data;
    }

    public void setData(Json data) {
        this.data = data;
    }
}
