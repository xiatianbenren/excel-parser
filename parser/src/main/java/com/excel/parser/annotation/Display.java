package com.excel.parser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于解析excel文件导入时表头和实体类属性对应关系
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Display {

    //实体类属性名对应的excel表头名称,此属性有值表示赋值的对象中这个属性的值来源于excel
    String value() default "";

    //实体类属性业务默认值，此属性有值表示赋值的对象中这个属性的值来源于注解
    String constValue() default "";
}
