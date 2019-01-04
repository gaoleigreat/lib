package com.lego.survey.lib.mybatis.injector;

/**
 * @author yanglf
 * @description
 * @since 2019/1/4
 **/
public enum MySqlMethod {

    DELETE_ALL("deleteAll", "根据 entity 条件删除记录", "<script>\nDELETE FROM %s %s\n</script>"),
    LOGIC_DELETE_ALL("deleteAll", "根据 entity 条件逻辑删除记录", "<script>\nUPDATE %s %s %s\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

    private MySqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return this.method;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getSql() {
        return this.sql;
    }
}

