package com.framework.mybatis.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.framework.common.page.Page;
import com.framework.common.page.PagedResult;

/**
 * @author : yanglf
 * @version : 1.0
 * @created IntelliJ IDEA.
 * @date : 2019/9/10 19:11
 * @desc :
 */
public class PageUtil {


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
        Page page=new Page();
        page.setPageIndex((int)iPage.getCurrent());
        page.setPageSize((int)iPage.getSize());
        page.setStartIndex(0);
        page.setTotalCount((int)iPage.getTotal());
        page.setTotalPages((int)iPage.getPages());
        pagedResult.setPage(page);
        return pagedResult;
    }


}
