package com.excel.parser.factory;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Generator<T> {

    public T generate(){
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();

//        ClassUtils.getClass()
        return null;
    }

    public static void main(String[] args) {
        List<String> list= new ArrayList<>();
        Type[] genericInterfaces = list.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            System.out.println(genericInterface);
            System.out.println(((ParameterizedType) genericInterface).getActualTypeArguments());
        }
    }
}
