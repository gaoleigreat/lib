package com.framework.mybatis.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@Configuration
public class DataSourceConfig {


    @Value("${define.datasource.type}")
    private Class<? extends DataSource> dataSourceType;


    /**
     * 主数据源
     *
     * @return
     */
    @Bean(name = "writeDataSource", destroyMethod = "close", initMethod = "init")
    @Primary
    @ConfigurationProperties(prefix = "define.datasource.write")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    /**
     * 共享数据源
     *
     * @return
     */
    @Bean(name = "shareDataSource", destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "define.share.datasource")
    public DataSource shareDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }


    /**
     * 从数据源1
     *
     * @return
     */
    @Bean(name = "readDataSource1")
    @ConfigurationProperties(prefix = "define.datasource.read1")
    public DataSource readDataSource1() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    /**
     * @return
     */
    @Bean(name = "readDataSources")
    public List<DataSource> readDataSources() {
        List<DataSource> dataSources = new ArrayList<>();
        dataSources.add(readDataSource1());
        return dataSources;
    }
}
