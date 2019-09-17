package com.framework.mybatis.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author : yanglf
 * @version : 1.0
 * @created IntelliJ IDEA.
 * @date : 2019/9/17 16:36
 * @desc :
 */
public class MyServiceImpl<M extends Mapper<T>, T> extends ServiceImpl<M, T> {


    public Mapper<T> getMapper() {
        return this.baseMapper;
    }


}
