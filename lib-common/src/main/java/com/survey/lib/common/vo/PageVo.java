package com.survey.lib.common.vo;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/29
 **/
public class PageVo<T> {
    private Long number; //当前页数
    private Long size; //页面大小
    private Boolean last; //是否最后一个
    private Long totalElements; //总记录数
    private Long totalPages; //总页数
    private Boolean first;//是否第一页
    private Long numberOfElements;
    private List<T> content; //分页内容
}
