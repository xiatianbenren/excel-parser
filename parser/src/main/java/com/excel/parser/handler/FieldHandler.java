package com.excel.parser.handler;

import java.lang.reflect.Field;

public interface FieldHandler {

    /**
     * 对指定属性对应的excel内容进行处理
     * @param field Java实体类属性
     * @param value 处理前单元格内容
     * @return excel处理后单元格内容
     */
    public String intercept(Field field,String value);
}
