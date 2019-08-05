package com.lego.survey.lib.mybatis.interceptor;

import com.lego.survey.lib.mybatis.boundsql.SurveyBoundSql;
import com.survey.lib.common.page.Page;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.PageVo;
import lombok.Data;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author yanglf
 * @description
 * @since 2019/2/25
 **/
@Intercepts({
        @Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class})})
@Data
public class PageInterceptor implements Interceptor {

    private Log log = LogFactory.getLog(PageInterceptor.class);

    //数据库类型，不同的数据库有不同的分页方法
    private String databaseType;

  /*  @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //对于StatementHandler其实只有两个实现类，一个是RoutingStatementHandler，另一个是抽象类BaseStatementHandler，
        //BaseStatementHandler有三个子类，分别是SimpleStatementHandler，PreparedStatementHandler和CallableStatementHandler，
        //SimpleStatementHandler是用于处理Statement的，PreparedStatementHandler是处理PreparedStatement的，而CallableStatementHandler是
        //处理CallableStatement的。Mybatis在进行Sql语句处理的时候都是建立的RoutingStatementHandler，而在RoutingStatementHandler里面拥有一个
        //StatementHandler类型的delegate属性，RoutingStatementHandler会依据Statement的不同建立对应的BaseStatementHandler，即SimpleStatementHandler、
        //PreparedStatementHandler或CallableStatementHandler，在RoutingStatementHandler里面所有StatementHandler接口方法的实现都是调用的delegate对应的方法。
        //我们在PageInterceptor类上已经用@Signature标记了该Interceptor只拦截StatementHandler接口的prepare方法，又因为Mybatis只有在建立RoutingStatementHandler的时候
        //是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是RoutingStatementHandler对象。
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        //通过反射获取到当前RoutingStatementHandler对象的delegate属性
        StatementHandler delegate = (StatementHandler)ReflectUtil.getFieldValue(handler, "delegate");
        //获取到当前StatementHandler的 boundSql，这里不管是调用handler.getBoundSql()还是直接调用delegate.getBoundSql()结果是一样的，因为之前已经说过了
        //RoutingStatementHandler实现的所有StatementHandler接口方法里面都是调用的delegate对应的方法。
        BoundSql boundSql = delegate.getBoundSql();
        //拿到当前绑定Sql的参数对象，就是我们在调用对应的Mapper映射语句时所传入的参数对象
        Object obj = boundSql.getParameterObject();
        //这里我们简单的通过传入的是Page对象就认定它是需要进行分页操作的。
        if (obj instanceof PageVo) {
            PageVo page = (PageVo) obj;
            //通过反射获取delegate父类BaseStatementHandler的mappedStatement属性
            MappedStatement mappedStatement = (MappedStatement)ReflectUtil.getFieldValue(delegate, "mappedStatement");
            //拦截到的prepare方法参数是一个Connection对象
            Connection connection = (Connection)invocation.getArgs()[0];
            //获取当前要执行的Sql语句，也就是我们直接在Mapper映射语句中写的Sql语句
            String sql = boundSql.getSql();
            //给当前的page参数对象设置总记录数
            this.setTotalRecord(page,
                    mappedStatement, connection);
            //获取分页Sql语句
            String pageSql = this.getPageSql(page, sql);
            //利用反射设置当前BoundSql对应的sql属性为我们建立好的分页Sql语句
            ReflectUtil.setFieldValue(boundSql, "sql", pageSql);
        }
        return invocation.proceed();
    }*/


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Executor executor = (Executor) invocation.getTarget();

        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];
        String sqlId = mappedStatement.getId();
        if (sqlId.endsWith("Count")) {

            return invocation.proceed();
        }

        Page page = null;
        if (parameter instanceof Map) {
            Map paraMap = (Map) parameter;
            for (Object obj : paraMap.values()) {
                if (obj instanceof Page) {
                    page = (Page) obj;
                    break;
                }
            }
        }

        if (parameter instanceof Page) {
            page = (Page) parameter;
        }

        if (null == page) {
            return invocation.proceed();
        }

        Configuration configuration = mappedStatement.getConfiguration();
        String countSqlId = sqlId + "Count";
        MappedStatement countMappedStatement = configuration.getMappedStatement(countSqlId);

        final RowBounds rowBounds = (RowBounds) args[2];
        final ResultHandler resultHandler = (ResultHandler) args[3];
        BoundSql boundSqlCount = countMappedStatement.getBoundSql(parameter);
        boundSqlCount = new SurveyBoundSql(configuration, boundSqlCount);
        StatementHandler statementHandlerCount = configuration.newStatementHandler(executor, countMappedStatement,
                parameter, rowBounds, resultHandler, boundSqlCount);
        Connection conn = ConnectionLogger.newInstance(executor.getTransaction().getConnection(), log, 1);

        List resultList = null;
        try (Statement stat = statementHandlerCount.prepare(conn, 120)) {
            statementHandlerCount.parameterize(stat);
            resultList = statementHandlerCount.query(stat, resultHandler);
        } catch (Exception e) {
            log.error("page interceptor error|", e);
        }
        Integer totalCount = 0;
        if (!CollectionUtils.isEmpty(resultList)) {
            totalCount = Integer.parseInt(resultList.get(0)+"");
        }
        page.setTotalCount(totalCount);
        PagedResult<List> pagedResult = new PagedResult<>();
        pagedResult.setPage(page);

        if (null == totalCount || 0 == totalCount || totalCount <= page.getStartIndex()) {
            pagedResult.setResultList(new ArrayList<>());
            List<PagedResult> result = new ArrayList<>();
            result.add(pagedResult);
            return result;
        }

        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        StatementHandler statementHandler = configuration.newStatementHandler(executor, mappedStatement, parameter,
                rowBounds, resultHandler, boundSql);

        Statement stat1 = null;
        try {
            stat1 = statementHandler.prepare(conn, 120);
            statementHandler.parameterize(stat1);
            resultList = statementHandler.query(stat1, resultHandler);
            pagedResult.setResultList(resultList);
        } catch (Exception e) {
            log.error("page interceptor error|", e);
        }
        List<PagedResult> result = new ArrayList<>();
        result.add(pagedResult);
        return result;
    }

    /**
     * 根据page对象获取对应的分页查询Sql语句，这里只做了两种数据库类型，Mysql和Oracle
     * 其它的数据库都 没有进行分页
     *
     * @param page 分页对象
     * @param sql  原sql语句
     * @return
     */
    private String getPageSql(PageVo<?> page, String sql) {
        StringBuffer sqlBuffer = new StringBuffer(sql);
        if ("mysql".equalsIgnoreCase(databaseType)) {
            return getMysqlPageSql(page, sqlBuffer);
        } else if ("oracle".equalsIgnoreCase(databaseType)) {
            return getOraclePageSql(page, sqlBuffer);
        }
        return sqlBuffer.toString();
    }


    /**
     * 获取Mysql数据库的分页查询语句
     *
     * @param page      分页对象
     * @param sqlBuffer 包含原sql语句的StringBuffer对象
     * @return Mysql数据库分页语句
     */
    private String getMysqlPageSql(PageVo<?> page, StringBuffer sqlBuffer) {
        //计算第一条记录的位置，Mysql中记录的位置是从0开始的。
        int offset = (page.getPageNo() - 1) * page.getPageSize();
        sqlBuffer.append(" limit ").append(offset).append(",").append(page.getPageSize());
        return sqlBuffer.toString();
    }

    /**
     * 获取Oracle数据库的分页查询语句
     *
     * @param page      分页对象
     * @param sqlBuffer 包含原sql语句的StringBuffer对象
     * @return Oracle数据库的分页查询语句
     */
    private String getOraclePageSql(PageVo<?> page, StringBuffer sqlBuffer) {
        //计算第一条记录的位置，Oracle分页是通过rownum进行的，而rownum是从1开始的
        int offset = (page.getPageNo() - 1) * page.getPageSize() + 1;
        sqlBuffer.insert(0, "select u.*, rownum r from (").append(") u where rownum < ").append(offset + page.getPageSize());
        sqlBuffer.insert(0, "select * from (").append(") where r >= ").append(offset);
        //上面的Sql语句拼接之后大概是这个样子：
        //select * from (select u.*, rownum r from (select * from t_user) u where rownum < 31) where r >= 16
        return sqlBuffer.toString();
    }


    /**
     * 给当前的参数对象page设置总记录数
     *
     * @param page            Mapper映射语句对应的参数对象
     * @param mappedStatement Mapper映射语句
     * @param connection      当前的数据库连接
     */
    private void setTotalRecord(PageVo page,
                                MappedStatement mappedStatement, Connection connection) {
        //获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
        //delegate里面的boundSql也是通过mappedStatement.getBoundSql(paramObj)方法获取到的。
        BoundSql boundSql = mappedStatement.getBoundSql(page);
        //获取到我们自己写在Mapper映射语句中对应的Sql语句
        String sql = boundSql.getSql();
        //通过查询Sql语句获取到对应的计算总记录数的sql语句
        String countSql = this.getCountSql(sql);
        //通过BoundSql获取对应的参数映射
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        //利用Configuration、查询记录数的Sql语句countSql、参数映射关系parameterMappings和参数对象page建立查询记录数对应的BoundSql对象。
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, page);
        //通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);
        //通过connection建立一个countSql对应的PreparedStatement对象。
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(countSql);
            //通过parameterHandler给PreparedStatement对象设置参数
            parameterHandler.setParameters(pstmt);
            //之后就是执行获取总记录数的Sql语句和获取结果了。
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int totalRecord = rs.getInt(1);
                //给当前的参数page对象设置总记录数
                page.setTotalRecord(totalRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据原Sql语句获取对应的查询总记录数的Sql语句
     *
     * @param sql
     * @return
     */
    private String getCountSql(String sql) {
        int index = sql.indexOf("from");
        return "select count(*) " + sql.substring(index);
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
