package com.lego.survey.lib.mybatis.injector;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
public class MySqlInjector extends AbstractSqlInjector  {

    /**
     * 获取注入的方法
     * @return
     */
    @Override
    public List<AbstractMethod> getMethodList() {
        return Stream.of(new DeleteAll()).collect(Collectors.toList());
    }
}
