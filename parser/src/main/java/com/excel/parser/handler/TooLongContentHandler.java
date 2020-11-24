package com.excel.parser.handler;

import com.excel.parser.handler.FieldHandler;

import java.lang.reflect.Field;

public class TooLongContentHandler implements FieldHandler {

    @Override
    public String intercept(Field field,String value) {
        if (field.getName().equals("VBELN")&&
            value.contains("/")){
            String[] split = value.split("/");
            value=split[0];
        }
        return value;
    }
}
