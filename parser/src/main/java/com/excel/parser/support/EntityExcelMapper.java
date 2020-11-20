package com.excel.parser.support;

import com.excel.parser.annotation.Display;
import com.excel.parser.annotation.EnableMapping;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("rawtypes")
@Component("entityExcelMapper")
//@Lazy(value = false)
@Slf4j
public class EntityExcelMapper implements BeanPostProcessor {
    @Autowired
    private MetaCollector collector;

    //支持excel映射注解的class集合
    @Getter
    private static List<Class> mapSupportedClasses= new ArrayList<>();
    //字段名和excel表头映射关系集合
    @Getter
    private static final List<ClassMetadata> fieldMappings =new ArrayList<>();

    public void mapEntityFieldDisplay(){
        log.debug("扫描实体所在包的路径，获取所有支持excel映射的实体");
        mapSupportedClasses= collector.collect();
        Iterator<Class> it = mapSupportedClasses.iterator();
        while(it.hasNext()){
            Class clz = it.next();
            if (!clz.isAnnotationPresent(EnableMapping.class)){
                it.remove();
                continue;
            }
            parseField(clz);
        }
    }

    private void parseField(Class clz){
        List<Field> fields = Arrays.asList(clz.getDeclaredFields());
        fields.forEach(field->{
            if (field.isAnnotationPresent(Display.class)){
                ClassMetadata me=new ClassMetadata();
                me.setFieldName(field.getName());
                me.setDisplayName(field.getAnnotation(Display.class).value());
                me.setBelongClassName(clz.getName());
                fieldMappings.add(me);
            }
        });
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("metaCollector")){
            mapEntityFieldDisplay();
        }
        return bean;
    }
}
