package com.framework.mybatis.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.page.Page;
import com.framework.common.page.PagedResult;
import com.framework.mybatis.mapper.Mapper;
import com.framework.mybatis.tool.WhereEntityTool;

/**
 * @author : yanglf
 * @version : 1.0
 * @created IntelliJ IDEA.
 * @date : 2019/9/10 19:11
 * @desc :
 */
public class PageUtil<T> {


    /**
     * @param page
     * @return
     */
    public static IPage page2IPage(Page page) {
        IPage iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page();
        iPage.setCurrent(page.getPageIndex());
        iPage.setSize(page.getPageSize());
        return iPage;
    }


    /**
     * @param iPage
     * @return
     */
    public static PagedResult iPage2Result(IPage iPage) {
        PagedResult pagedResult = new PagedResult();
        pagedResult.setResultList(iPage.getRecords());
        Page page = new Page();
        page.setPageIndex((int) iPage.getCurrent());
        page.setPageSize((int) iPage.getSize());
        page.setStartIndex(0);
        page.setTotalCount((int) iPage.getTotal());
        page.setTotalPages((int) iPage.getPages());
        pagedResult.setPage(page);
        return pagedResult;
    }


    /**
     * 分頁查詢
     *
     * @param page
     * @param o
     * @param mapper
     * @return
     */
    public static PagedResult queryPaged(Page page, Object o, Mapper mapper) {
        IPage iPage = page2IPage(page);
        QueryWrapper wrapper = new QueryWrapper();
        WhereEntityTool.invoke(o, wrapper);
        IPage selectPage = mapper.selectPage(iPage, wrapper);
        return iPage2Result(selectPage);
    }


}
