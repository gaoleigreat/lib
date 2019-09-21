package com.framework.mybatis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    private final int readDataSourceSize;
    private AtomicInteger count = new AtomicInteger(0);

    DynamicDataSource(int readDataSourceSize) {
        this.readDataSourceSize = readDataSourceSize;
    }


    /**
     * 获取当前数据源connection
     *
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws SQLException {
        return super.getConnection();
    }

    /**
     * 如果不希望数据源在启动配置时就加载好，可以定制这个方法，从任何你希望的地方读取并返回数据源
     * 比如从数据库、文件、外部接口等读取数据源信息，并最终返回一个DataSource实现类对象即可
     *
     * @return
     */
    @Override
    protected DataSource determineTargetDataSource() {
        // 加载数据源   会调用  determineCurrentLookupKey() 获取数据源 key
        return super.determineTargetDataSource();
    }

    /**
     * 查询数据源的  key
     * 如果希望所有数据源在启动配置时就加载好，这里通过设置数据源Key值来切换数据，定制这个方法
     * 负载原则：未指定数据源则负载至[从数据源]
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String typeKey = DataSourceContextHolder.getJdbcType();
        if (typeKey == null) {
            log.debug("未指定数据源：");
            return lookupKey();
        }
        if (DataSourceType.write.getType().equals(typeKey)) {
            log.debug("负载至数据源 -> 主");
            return DataSourceType.write.getType();
        }
        if (DataSourceType.share.getType().equals(typeKey)) {
            log.debug("切换到共享数据源 -->>");
            return DataSourceType.share.getType();
        }
        return lookupKey();
    }


    /**
     * 设置默认的数据源
     *
     * @param defaultTargetDataSource defaultTargetDataSource
     */
    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
    }


    /**
     * 新增数据源
     *
     * @param targetDataSources
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
    }


    /**
     * 从库负载均衡
     *
     * @return
     */
    private Integer lookupKey() {
        // 读,简单轮询负载均衡
        int number = count.getAndAdd(1);
        int lookupKey = number % readDataSourceSize;
        log.debug("负载至数据源 -> 从" + (lookupKey + 1));
        return lookupKey;
    }
}
