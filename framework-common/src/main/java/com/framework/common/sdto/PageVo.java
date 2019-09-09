package com.framework.common.sdto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanglf
 * @description
 * @since 2018/12/29
 **/
public class PageVo<T> {
    //页码，默认是第一页
    private int pageNo = 1;
    //每页显示的记录数，默认是15
    private int pageSize = 15;
    //总记录数
    private int totalRecord;
    //总页数
    private int totalPage;
    //对应的当前页记录
    private List<T> results;
    //其他的参数我们把它分装成一个Map对象
    private Map<String, Object> params = new HashMap<>();

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
        //在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
        int totalPage = totalRecord%pageSize==0 ? totalRecord/pageSize : totalRecord/pageSize + 1;
        this.setTotalPage(totalPage);
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Page [pageNo=" + pageNo + ", pageSize=" +
                pageSize + ", results=" + results +
                ", totalPage=" + totalPage +
                ", totalRecord=" + totalRecord + "]";
    }

}