package com.lego.survey.lib.mybatis.interceptor;

import com.lego.survey.lib.mybatis.sqlsource.SurveySqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @author yanglf
 * @description
 * @since 2019/2/25
 **/
@Intercepts({ @Signature(type = Executor.class, method = "query",
        args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class ProgramInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];

        SurveySqlSource surveySqlSource = new SurveySqlSource(mappedStatement.getSqlSource(), mappedStatement.getConfiguration());
        MappedStatement.Builder builder = new MappedStatement.Builder(mappedStatement.getConfiguration(), mappedStatement.getId(),
                surveySqlSource,
                mappedStatement.getSqlCommandType());

        builder.resource(mappedStatement.getResource());
        builder.parameterMap(mappedStatement.getParameterMap());
        builder.resultMaps(mappedStatement.getResultMaps());
        builder.fetchSize(mappedStatement.getFetchSize());
        builder.timeout(mappedStatement.getTimeout());
        builder.statementType(mappedStatement.getStatementType());
        builder.resultSetType(mappedStatement.getResultSetType());
        builder.cache(mappedStatement.getCache());
        builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
        builder.keyGenerator(mappedStatement.getKeyGenerator());
        builder.useCache(mappedStatement.isUseCache());
        args[0] = builder.build();
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
