package com.framework.mybatis.sqlsource;

import com.framework.mybatis.boundsql.SurveyBoundSql;
import lombok.Data;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

/**
 * @author yanglf
 * @description
 * @since 2019/2/25
 **/
@Data
public class LegoSqlSource implements SqlSource {

    private SqlSource sqlSource;

    private Configuration configuration;

    public LegoSqlSource(SqlSource sqlSource, Configuration configuration) {
        this.sqlSource = sqlSource;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new SurveyBoundSql(configuration, sqlSource.getBoundSql(parameterObject));
    }
}
