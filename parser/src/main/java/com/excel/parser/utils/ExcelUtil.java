package com.excel.parser.utils;

import com.excel.parser.support.ClassMetadata;
import com.excel.parser.support.EntityExcelMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ExcelUtil<T> {

    public static final String EXCEL_SUFFIX_XLS = "xls";
    public static final String EXCEL_SUFFIX_XLSX = "XLSX";
    public static final String EMPTY = "";
    public static final String POINT = ".";
    public static final String SPLIT_TOKEN="&";
    public int totalRows; //sheet中总行数
    public static int totalCells; //每一行总单元格数
    public Map<Integer,String> mapping= new HashMap<>();
    public static SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");

    public static Workbook getWorkBook(InputStream is, String fileName) throws IOException {
        //获得文件名后缀
        String fileLastName = fileName.substring(fileName.lastIndexOf(".")+1);
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
        if(fileLastName.endsWith(EXCEL_SUFFIX_XLS)){
            //2003
            workbook = new HSSFWorkbook(is);
        }else if(fileLastName.endsWith(EXCEL_SUFFIX_XLSX)){
            //2007
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
    }

    /**
     * 获得文件的后缀名
     * @param path
     * @return
     */
    public static String getSuffix(String path){
        if(path==null || EMPTY.equals(path.trim())){
            return EMPTY;
        }
        if(path.contains(POINT)){
            return path.substring(path.lastIndexOf(POINT)+1,path.length());
        }
        return EMPTY;
    }

    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellType().name()){
            case "NUMERIC": //数字
                if (DateUtil.isCellDateFormatted(cell)){
                    //用于转化为日期格式
                    Date d = cell.getDateCellValue();
                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = formater.format(d);
                }else{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case "STRING": //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case "BOOLEAN": //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case "FORMULA": //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case "BLANK": //空值
                cellValue = "";
                break;
            case "ERROR": //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public List<T> readExcel2Entity(InputStream inputStream,String fileName, T type) throws Exception{
        log.info("[获取到需要解析的excel文件fileName：]"+fileName);
        // 检查文件
        checkFile(inputStream,fileName);
        // 创建Workbook工作薄对象
        Workbook workbook = getWorkBook(inputStream,fileName);
        // 创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<T> list = new ArrayList<>();
        HashSet<T> set = new HashSet<>();
        if(workbook != null){
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){
                    continue;
                }
                int firstRowNum  = sheet.getFirstRowNum()+1;
                int lastRowNum = sheet.getLastRowNum();
                totalRows=lastRowNum;
                totalCells=sheet.getRow(0).getLastCellNum();
                for(int rowNum = 0;rowNum <= lastRowNum;rowNum++){
                    Row row = sheet.getRow(rowNum);
                    if(row == null){
                        continue;
                    }
                    //解析表头
                    if (rowNum==0){
                        parseHeader(row,type.getClass());
                        continue;
                    }
                    // getFirstCellNum可以判断当前行是否有数据  如果整行没有数据返回-1 只要当前行有一列有数据就会返回下标
                    int firstCellNum = row.getFirstCellNum();
                    Object obj = type.getClass().newInstance();
                    if(firstCellNum != -1){
                        loadInstance(row, obj);
                    }
                    if (allFieldIsNULL(obj))continue;//特殊行，所有属性都是空，跳过
                    set.add(((T) obj));
                }
            }
            workbook.close();
        }
        list.addAll(set);
        inputStream.close();
        return list;
    }

    public static boolean allFieldIsNULL(Object o){
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                Object object = field.get(o);
                if (object instanceof CharSequence) {
                    if (!org.springframework.util.ObjectUtils.isEmpty(object)) {
                        return false;
                    }
                } else {
                    if (null != object) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            log.error("判断对象属性为空异常", e);
        }
        return true;
    }


    @SuppressWarnings("rawtypes")
    private void parseHeader(Row header, Class type) {
        List<Class> classes = EntityExcelMapper.getMapSupportedClasses();
        List<ClassMetadata> fieldMappings = EntityExcelMapper.getFieldMappings();
        if (!classes.contains(type)){
            return;
        }
        List<ClassMetadata> newFieldMapping = fieldMappings.stream()
                .filter(o -> o.getBelongClassName().equals(type.getName()))
                .collect(Collectors.toList());
        for(int cellNum = 0 ; cellNum <= totalCells; cellNum++){
            // 获取每一列对应的数据库字段名称
            String value = getCellValue(header.getCell(cellNum));
            for (ClassMetadata fieldMapping : newFieldMapping) {
                if(!fieldMapping.getBelongClassName().equals(type.getName())){
                    continue;
                }
                if (fieldMapping.getDisplayName().equals(value)){
                    String fieldName = fieldMapping.getFieldName();
                    //如果一个excel字段对应多个属性，采用拼接的方式
                    if (mapping.containsKey(cellNum)){
                        String redudantName = mapping.get(cellNum) + SPLIT_TOKEN + fieldName;
                        mapping.put(cellNum,redudantName);
                    }else{
                        mapping.put(cellNum,fieldName);
                    }
                }
            }
        }
    }

    private void loadInstance(Row row, Object instance) throws Exception {
        for(int cellNum = 0 ; cellNum <= totalCells; cellNum++){
            Cell cell = row.getCell(cellNum);
            String fieldName = mapping.get(cellNum);
            if (null==fieldName)continue;
            if (fieldName.contains(SPLIT_TOKEN)){
                String[] fieldNames = fieldName.split(SPLIT_TOKEN);
                for (int i = 0; i < fieldNames.length; i++) {
                    String name = fieldNames[i];
                    generateModel(instance, name, getCellValue(cell));
                }
                continue;
            }
            generateModel(instance, fieldName, getCellValue(cell));
        }
    }
    /**
     * @Description: 反射获取set方法并赋值
     */
    public static void generateModel(Object instance, String filedName, String value) throws Exception {
        // 获取这个类中和这个字段相关的信息
        Field field = instance.getClass().getDeclaredField(filedName);
        // 设置这个字段可以被赋值  默认不能赋值
        field.setAccessible(true);
        transFieldValue( instance, field, value);
    }

    public static void transFieldValue(Object instance, Field field, String value) throws Exception {
        // 获取字段名，将字段名的首字符大写，方便构造get，set方法
        String name = field.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        String type = field.getGenericType().toString();

        if (type.equals("class java.lang.String")) {
            Method m = instance.getClass().getMethod("set"+name,String.class);
            if (StringUtils.isNotBlank(value.trim())) {
                m.invoke(instance, value);
                return;
            }
        }else if (type.equals("class java.lang.Integer")) {
            Method m = instance.getClass().getMethod("set"+name,Integer.class);
            if (StringUtils.isNotBlank(value.trim())) {
                m.invoke(instance, Integer.parseInt(value));
                return;
            }
        }else if (type.equals("class java.lang.Short")) {
            Method m = instance.getClass().getMethod("set"+name,Short.class);
            if (StringUtils.isNotBlank(value.trim())) {
                m.invoke(instance, Short.parseShort(value));
                return;
            }
        }else if (type.equals("class java.lang.Double")) {
            Method m = instance.getClass().getMethod("set"+name,Double.class);
            if (StringUtils.isNotBlank(value.trim())) {
                m.invoke(instance, Double.parseDouble(value));
                return;
            }
        }else if (type.equals("class java.math.BigDecimal")) {
            Method m = instance.getClass().getMethod("set"+name,BigDecimal.class);
            if (StringUtils.isNotBlank(value.trim())) {
                m.invoke(instance, new BigDecimal(value));
                return;
            }
        }else if (type.equals("class java.lang.Boolean")) {
            Method m = instance.getClass().getMethod("set"+name,Boolean.class);
            if (StringUtils.isNotBlank(value.trim())) {
                m.invoke(instance, Boolean.parseBoolean(value));
                return;
            }
        }else if (type.equals("class java.util.Date")) {
            Method m = instance.getClass().getMethod("set"+name,Date.class);
            if (StringUtils.isNotBlank(value.trim())) {
                SimpleDateFormat sdf_long = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                SimpleDateFormat sdf_short = new SimpleDateFormat("yyyy-MM-dd");
                Date date;
                if (value.length() > 10){
                    date = sdf_long.parse(value);
                }else {
                    date = sdf_short.parse(value);
                }
                m.invoke(instance, date);
                return;
            }
        }
    }

    /**
     * @Description: 文件完整性校验
     */
    @SneakyThrows
    public static void checkFile(InputStream is,String fileName) {
        log.info("[判断文件是否存在以及文件是否为excel]");
        //判断文件是否存在
        if(null == is){
            log.error("从oss上获取到的文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileLastName = fileName.substring(fileName.lastIndexOf(".")+1);
        //判断文件是否是excel文件
        if(!fileLastName.endsWith(EXCEL_SUFFIX_XLS) && !fileLastName.endsWith(EXCEL_SUFFIX_XLSX)){
            log.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }
}
