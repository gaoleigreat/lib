package com.framework.common.element;

/**
 *
 * office 替换数据模板
 */
public abstract class OObject {

    public abstract Object getValByKey(String key);

    public boolean isPic(String key) {
        return this.getValByKey(key) instanceof OPic;
    }

    public String getTextByKey(String key) {
        if (!isPic(key)) {
            return (String) this.getValByKey(key);
        } else {
            return null;
        }
    }

    public OPic getPicByKey(String key) {
        if (isPic(key)) {
            return (OPic) this.getValByKey(key);
        } else {
            return null;
        }
    }
}
