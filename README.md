# Getting Started
### excel解析框架：

- 基于注解驱动的excel解析方式，配置更灵活
- 泛型化解析，直接输出实体对象，使用更友好
- 输入支持http上传和io流读取文件两种方式
- 自定义表头和字段绑定功能
- 支持excel一列对应多个属性的场景
- 支持指定字段组合去重功能，适用于主子关系数据解析场景

### Guides
The following guides illustrate how to use some features concretely:

##### 配置：

1. 指定excel解析时扫描实体类的包路径，示例：

   ```java
   citicpub.oms.excel.entity-packages=com.citicpub.dataconvert.mybatisentity.oms
   ```

2. 实体类配置

     在实体类添加@EnableMapping注解，表头对应的属性上添加@Display注解，如：

     ```java
     @EnableMapping
        @EqualsAndHashCode
        public class JvSapTradeLogistics {
            private Long id;
            @Display("交货单号")
            @EqualsAndHashCode.Include
            private String VBELN;//交货单号
            @Display("销售订单")
            @EqualsAndHashCode.Include
            private String LIFEX;//外部单号
            @Display("销售订单")
            @EqualsAndHashCode.Include
            private String BSTKD;//采购订单号
            @Display("交货单创建日期")
            @EqualsAndHashCode.Include
            private Date WADAT_IST;//交货单过账日期
            private String WERKS;//发货工厂,发货仓库  中信ERP发货的仓库编号
            private String NAME1;//发货工厂名称    
        } 
     ```

     如果要支持excel数据生成对象时去重，需要在类型上添加lombok的@EqualsAndHashCode，并在需要对比的字段上添加@EqualsAndHashCode.Inclde或在不需要对比的字段上添加@EqualsAndHashCode.Exclude。   

3. API调用

     - 注入EntityExcelMapper

       ```java
       @Autowired
       EntityExcelMapper entityExcelMapper;
       ```

     - 进行元数据初始化

       ```java
       entityExcelMapper.prepare();
       ```

     - 声明泛型化的excelUtil，泛型为实体类类型

       ```java
        ExcelUtil excelUtil= new ExcelUtil<>(); 
       ```

     - 调用readExcel2Entity()方法进行解析

       ```
       List logistics = excelUtil.readExcel2Entity(in, fileName, new JvSapTradeLogistics());
       ```

       