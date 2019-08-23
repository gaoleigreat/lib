package com.framework.mybatis.plugin;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
@Slf4j
@Component
public class GenerateCodePlugin {

    @Value("${mybatis_plus.datasource.mysql.url:jdbc}")
    private String dataSourceUrl;


    @Value("${mybatis_plus.datasource.mysql.password:admin123}")
    private String dataSourcePwd;


    @Value("${mybatis_plus.datasource.mysql.username:root}")
    private String dataSourceUserName;


    @Value("${mybatis_plus.datasource.mysql.driver_class:com}")
    private String dataSourceDriverClass;


    @Value("${mybatis_plus.datasource.mysql.output_dir:d:/out}")
    private String outputDir;


    @Value("${mybatis_plus.datasource.mysql.entity_package:com.entity}")
    private String entityPackage;


    @Value("${mybatis_plus.datasource.mysql.mapper_package:com.mapper}")
    private String mapperPackage;


    @Value("${mybatis_plus.datasource.mysql.mapper_xml_package:mapper}")
    private String mapperXmlPackage;


    @Value("${mybatis_plus.datasource.mysql.service_package:com.service}")
    private String servicePackage;


    @Value("${mybatis_plus.datasource.mysql.service_impl_package:com.service.impl}")
    private String serviceImplPackage;


    @Value("${mybatis_plus.datasource.mysql.controller_package:com.controller}")
    private String controllerPackage;


    public void generateCode(String[] tableNames) {
        AutoGenerator autoGenerator = new AutoGenerator();

        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //输出文件路径
        globalConfig.setOutputDir(outputDir);
        //覆盖已有文件
        globalConfig.setFileOverride(true);
        //是否需要 ActiveRecord(AR模式) 属性
        globalConfig.setActiveRecord(true);
        // XML 二级缓存
        globalConfig.setEnableCache(false);
        // XML ResultMap
        globalConfig.setBaseResultMap(true);
        // XML ColumnList
        globalConfig.setBaseColumnList(true);
        // 类 作者
        globalConfig.setAuthor("yanglf");
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("I%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sController");
        autoGenerator.setGlobalConfig(globalConfig);

        //数据源
        DataSourceConfig dataSourceConfig = getDataSourceConfig();
        autoGenerator.setDataSource(dataSourceConfig);

        //策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        //  表名策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        //  需要生成的表名
        strategyConfig.setInclude(tableNames);
        // 自定义 dao的父类
        strategyConfig.setSuperMapperClass("Mapper");
        autoGenerator.setStrategy(strategyConfig);

        //包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(null);
        packageConfig.setEntity(entityPackage);
        packageConfig.setMapper(mapperPackage);
        packageConfig.setXml(mapperXmlPackage);
        packageConfig.setService(servicePackage);
        packageConfig.setServiceImpl(serviceImplPackage);
        packageConfig.setController(controllerPackage);
        autoGenerator.setPackageInfo(packageConfig);
        //执行生成
        autoGenerator.execute();
        log.info("execute over....");
    }


    private DataSourceConfig getDataSourceConfig() {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(globalConfig,fieldType);
            }
        });
        dsc.setDriverName(dataSourceDriverClass);
        dsc.setUsername(dataSourceUserName);
        dsc.setPassword(dataSourcePwd);
        dsc.setUrl(dataSourceUrl);
        return dsc;
    }

}
