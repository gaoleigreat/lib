package com.framework.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.framework.mybatis.injector.MySqlInjector;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.*;

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

    static final String PACKAGE = "com.lego.**.mapper";
    static final String DOMAIN = "com.lego.**.entity";
    static final String MAPPER_LOCATION = "classpath:mapper/**/*.xml";

    @Value("${define.datasource.type}")
    private Class<? extends DataSource> dataSourceType;
    @Resource(name = "writeDataSource")
    private DataSource writeDataSource;
    @Resource(name = "shareDataSource")
    private DataSource shareDataSource;

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
        org.apache.ibatis.session.Configuration configuration = Objects.requireNonNull(sqlSessionFactoryBean.getObject()).getConfiguration();
        // 开启自动驼峰规则
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setCallSettersOnNulls(false);
        Interceptor[] list = new Interceptor[2];
        //分页插件
        list[0] = paginationInterceptor();
        list[1] = performanceInterceptor();
        // 执行分析插件  阻止删除整表
        // list[2] = sqlExplainInterceptor();
        // 乐观锁插件
        // list[3] = optimisticLockerInterceptor();
        sqlSessionFactoryBean.setPlugins(list);
        //plugs- 配置全局配置
        sqlSessionFactoryBean.setGlobalConfig(globalConfiguration());
        return sqlSessionFactoryBean;
    }


    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType(DbType.MYSQL.getDb());
        List<ISqlParser> parserList = new ArrayList<>();
        parserList.add(new BlockAttackSqlParser());

        paginationInterceptor.setSqlParserList(parserList);
        return paginationInterceptor;
    }


    @Bean
    @Profile({"dev", "local", "sit"})
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        // 格式化sql
        performanceInterceptor.setFormat(true);
        performanceInterceptor.setMaxTime(2000);
        return performanceInterceptor;
    }


    /*@Bean
    public  OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }*/


    /*@Bean
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();
        Properties prop = new Properties();
        prop.setProperty("stopProceed", "true");
        sqlExplainInterceptor.setProperties(prop);
        return sqlExplainInterceptor;
    }*/


 /*   @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("helperDialect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }*/


    @Bean
    public GlobalConfig globalConfiguration() {
        GlobalConfig globalConfiguration = new GlobalConfig();
        // AUTO->`0`("数据库ID自增")
        // INPUT->`1`(用户输入ID")
        // ID_WORKER->`2`("全局唯一ID")
        // UUID->`3`("全局唯一ID")
        GlobalConfig.DbConfig config = new GlobalConfig.DbConfig();
        config.setDbType(DbType.MYSQL);
        // 主键类型  0:"数据库ID自增", 1:"用户输入ID",2:"全局唯一ID (数字类型唯一ID)", 3:"全局唯一ID UUID";
        config.setIdType(IdType.INPUT);
        config.setTablePrefix("tpl_");
        config.setTableUnderline(true);
        globalConfiguration.setDbConfig(config);
        globalConfiguration.setBanner(false);
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
        return globalConfiguration;
    }


    @Bean
    public AbstractRoutingDataSource roundRobinDataSouceProxy() {
        DynamicDataSource proxy = new DynamicDataSource(readDataSources.size());
        Map<Object, Object> targetDataSources = new HashMap<>();

        targetDataSources.put(DataSourceType.write.getType(), writeDataSource);
        targetDataSources.put(DataSourceType.share.getType(),shareDataSource);

        //多个读数据库
        for (int i = 0; i < readDataSources.size(); i++) {
            targetDataSources.put(DataSourceType.read.getType() + i, readDataSources.get(i));
        }
        proxy.setDefaultTargetDataSource(writeDataSource);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

    @Bean
    public MySqlInjector mySqlInjector() {
        return new MySqlInjector();
    }


}
