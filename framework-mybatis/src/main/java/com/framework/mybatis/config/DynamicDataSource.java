package com.framework.mybatis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.SQLException;
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
     * 负载原则：未指定数据源则负载至[从数据源]
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String typeKey = DataSourceContextHolder.getJdbcType();
        if(typeKey == null) {
            log.debug("未指定数据源：");
            return lookupKey();
        }
        if (DataSourceType.write.getType().equals(typeKey)) {
            log.debug("负载至数据源 -> 主");
            return DataSourceType.write.getType();
        }
        if(DataSourceType.share.getType().equals(typeKey)){
            log.debug("切换到共享数据源 -->>");
            return DataSourceType.share.getType();
        }
        return lookupKey();
    }

    /**
     * 从库负载均衡
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
