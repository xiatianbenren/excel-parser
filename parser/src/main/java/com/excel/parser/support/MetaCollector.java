package com.excel.parser.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component("metaCollector")
@Slf4j
public class MetaCollector {

    @Value("${citicpub.oms.excel.entity-packages}")
    private String packages;

    //包扫描
    public List<Class> collect(){
        log.debug("开始扫描");
        List<Class> list= new ArrayList<>();
        Arrays.asList(packages.split(",")).forEach(pac->{
            log.debug("当前扫描包路径为：{}",pac);
            ClassScanner scanner = new ClassScanner(pac, true, null, null);
            try {
                Set<Class<?>> classes = scanner.doScanAllClasses();
                list.addAll(classes);
            }  catch (Exception e) {
                log.error("自定义包扫描出错");
                e.printStackTrace();
            }
        });
        return list;
    }
}
