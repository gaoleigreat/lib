package com.lego.survey.lib.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.lego.survey.lib.mybatis.injector.MySqlInjector;
import com.lego.survey.lib.mybatis.interceptor.ProgramInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Slf4j
@Configuration
@ConditionalOnClass({EnableTransactionManagement.class})
@MapperScan(basePackages = {MybatisConfig.PACKAGE})
public class MybatisConfig {

    static final   String PACKAGE = "com.lego.**.mapper";
    static final String DOMAIN = "com.lego.**.entity";
    static final String MAPPER_LOCATION = "classpath:mapper/**/*.xml";

    @Value("${define.datasource.type}")
    private Class<? extends DataSource> dataSourceType;
    @Resource(name = "writeDataSource")
    private DataSource writeDataSource;
    @Resource(name = "readDataSources")
    private List<DataSource> readDataSources;

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory() throws Exception {
        log.debug("-----------------------sqlSessionFactory init.-----------------------");
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(roundRobinDataSouceProxy());
        sqlSessionFactoryBean.setTypeAliasesPackage(DOMAIN);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        //分页插件
        PaginationInterceptor paginationInterceptor=new PaginationInterceptor();
        paginationInterceptor.setDialectType(DbType.MYSQL.getDb());
        Interceptor[] list=new Interceptor[5];
        list[0]=paginationInterceptor;
        //TODO 性能分析插件
        PerformanceInterceptor performanceInterceptor=new PerformanceInterceptor();
        // 格式化sql
        performanceInterceptor.setFormat(true);
        performanceInterceptor.setMaxTime(2000);
        list[1]=performanceInterceptor;
        //TODO 执行分析插件  阻止删除整表
        SqlExplainInterceptor sqlExplainInterceptor=new SqlExplainInterceptor();
        Properties prop=new Properties();
        prop.setProperty("stopProceed","true");
        sqlExplainInterceptor.setProperties(prop);
        list[2]=sqlExplainInterceptor;
        // 乐观锁插件
        OptimisticLockerInterceptor optimisticLockerInterceptor=new OptimisticLockerInterceptor();
        list[3]=optimisticLockerInterceptor;
        //TODO
        ProgramInterceptor programInterceptor=new ProgramInterceptor();
        list[4]=programInterceptor;
        sqlSessionFactoryBean.setPlugins(list);
        //plugs- 配置全局配置
        sqlSessionFactoryBean.setGlobalConfig(globalConfiguration());
        return sqlSessionFactoryBean;
    }




    @Bean
    public GlobalConfig globalConfiguration(){
        GlobalConfig globalConfiguration=new GlobalConfig();
        // AUTO->`0`("数据库ID自增")
        // INPUT->`1`(用户输入ID")
        // ID_WORKER->`2`("全局唯一ID")
        // UUID->`3`("全局唯一ID")
        GlobalConfig.DbConfig config=new GlobalConfig.DbConfig();
        config.setDbType(DbType.MYSQL);
        config.setIdType(IdType.INPUT);
        config.setTableUnderline(true);
        globalConfiguration.setDbConfig(config);
      //  globalConfiguration.setSqlInjector(mySqlInjector());

        //  MYSQL->`mysql`
        //  ORACLE->`oracle`
        //   DB2->`db2`
        // H2->`h2`
        // HSQL->`hsql`
        //  SQLITE->`sqlite`
        //   POSTGRE->`postgresql`
        //  SQLSERVER2005->`sqlserver2005`
        //  SQLSERVER->`sqlserver`
        // Oracle需要添加该项
        //  <property name="dbType" value="oracle" />
        //  全局表为下划线命名设置 true
        return  globalConfiguration;
    }


    @Bean
    public AbstractRoutingDataSource roundRobinDataSouceProxy() {
        DynamicDataSource proxy = new DynamicDataSource(readDataSources.size());
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.write.getType(), writeDataSource);
        //多个读数据库
        for (int i = 0; i < readDataSources.size(); i++) {
            targetDataSources.put(i, readDataSources.get(i));
        }
        proxy.setDefaultTargetDataSource(writeDataSource);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }


    @Bean
    public MySqlInjector mySqlInjector(){
        return new MySqlInjector();
    }


}
