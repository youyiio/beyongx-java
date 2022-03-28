package com.beyongx.bootstrap;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {


    /**
     * 主入口
     *
     * @param args
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // >>>输入配置，moduleName
        

        String moduleName = scanner("请输入子模块名称(小写, 例xxx):", false);
        String moduleFolder = scanner("请输入子模块文件夹(小写, 例beyongx-xxx):", false);
        String modulePackageName = scanner("请输入子模块包名(小写，例com.beyongx.xxx):", false);
        

        System.out.println("Create new Module: " + moduleName);
        System.out.println("Module's Package name: " + modulePackageName);
        //System.exit(0);
        
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + "/" + moduleFolder;
        System.out.println(projectPath);
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("youyi.io");
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        String profile = scanner("请输入使用的profile配置(小写, 例dev|test|prod):", false);
        Map<String, Object> ymlMap = loadYaml(profile);
        Map<String, Object> datasource = (Map<String, Object>)((Map<String, Object>)ymlMap.get("spring")).get("datasource");
        Map<String, Object> dbConfig = (Map<String, Object>)((Map<String, Object>)datasource.get("druid")).get("db0");
        String driverName = (String)dbConfig.get("driver-class-name");
        String url = (String)dbConfig.get("url");
        String username = (String)dbConfig.get("username");
        String password = (String)dbConfig.get("password");
        dataSourceConfig.setDriverName(driverName);
        dataSourceConfig.setUrl(url);
        // dataSourceConfig.setSchemaName((String)dbConfig.get("dbname"));
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        mpg.setDataSource(dataSourceConfig);

        //数据库字段类型映射
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                //将数据库中datetime转换成date
                if (fieldType.toLowerCase().contains("datetime")) {
                    return DbColumnType.DATE;
                }

                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
            }
        });

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(moduleName);
        String moduleRootPackageName = modulePackageName.substring(0, modulePackageName.lastIndexOf("."));
        packageConfig.setParent(moduleRootPackageName);
        mpg.setPackageInfo(packageConfig);

        // 自定义注入代码的配置
        String tablePrefix = scanner("请输入表名前缀，例sys:", false);
        List<String> tableNameList = readTableNames(driverName, url, username, password, tablePrefix);
        String entityRemoveTablePrefix = scanner("Mybatis生成的实体是否去掉表前缀，例y|n:", false);
        String generateController = scanner("Mybatis生成的实体是否生成对应的Controller，例y|n:", true);

        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                String filePath = projectPath + "/src/main/resources/mapper/" + packageConfig.getModuleName();
                if (!new File(filePath).exists()) {
                    new File(filePath).mkdirs();
                }
                return projectPath + "/src/main/resources/mapper/" + packageConfig.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        //文件创建策略
        injectionConfig.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                //如果是Entity则直接返回true表示写文件
                if (fileType == FileType.ENTITY) {
                    return true;
                }

                // 判断自定义文件夹是否需要创建
                //checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                if (fileType == FileType.SERVICE || fileType == FileType.SERVICE_IMPL) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                if (fileType == FileType.CONTROLLER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return generateController.trim().equalsIgnoreCase("y") && !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });

        injectionConfig.setFileOutConfigList(focList);
        mpg.setCfg(injectionConfig);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        
        String[] tableNames = new String[]{};
        strategy.setInclude(tableNameList.toArray(tableNames));
        if (entityRemoveTablePrefix.trim().equalsIgnoreCase("y")) {
            //生成的实体策略去掉的表前缀
            strategy.setTablePrefix(packageConfig.getModuleName() + "_");
        }

        strategy.setControllerMappingHyphenStyle(true);        
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

        System.out.println("生成成功！");
    }

    /**
     * <p>
     * 读取控制台内容 quetion and answer
     * </p>
     */
    public static String scanner(String tip, boolean closeConsole) {
        System.out.println(tip);

        Scanner scanner = null;
        String input = "";
        try {
            scanner = new Scanner(System.in, "UTF-8");
        
            // if (scanner.hasNext()) {
            //     String ipt = scanner.next();
            //     if (StringUtils.isNotBlank(ipt)) {
            //         return ipt;
            //     }
            //     return ipt;
            // }
            // throw new MybatisPlusException("请输入正确的" + tip + "！");
            
            input = scanner.nextLine(); //允许空返回
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (scanner != null && closeConsole) {
                scanner.close();
            }
        }

        return input;
    }

    /**
     * 创建新模块
     * @param moduleFolder
     * @param modulePackageName
     * @return
     */
    public static boolean createModule(String moduleFolder, String modulePackageName) {
        return false;
    }

    /**
     * 读取profile的yaml配置文件
     * @param profile
     * @return
     */
    public static Map<String, Object> loadYaml(String profile) {
        Map<String, Object> map = null;
        try {
            Yaml yaml = new Yaml();
            
            URL url = CodeGenerator.class.getClassLoader().getResource("application-" + profile + ".yml");
            if (url != null) {
                //获取yaml文件中的配置数据将值转换为Map
                map = (Map<String, Object>)yaml.load(new FileInputStream(url.getFile()));                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 读取table prefix的表集合
     * @param driverName
     * @param url
     * @param username
     * @param password
     * @param tablePrefix
     * @return
     */
    public static List<String> readTableNames(String driverName, String url, String username, String password, String tablePrefix) {
        List<String> tableNames = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {   
            //加载数据库的驱动类   
            Class.forName(driverName);

            conn = DriverManager.getConnection(url , username , password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from information_schema.TABLES") ;
            while(rs.next()) {
                String tableName = rs.getString("table_name") ;   
                tableNames.add(tableName);
            }
        } catch(ClassNotFoundException e) {
            System.out.println("找不到驱动程序类，加载驱动失败！");   
            e.printStackTrace();   
        } catch(SQLException se) {   
            System.out.println("数据库连接失败！");   
            se.printStackTrace() ;   
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        String trimTablePrefix = tablePrefix.trim();
        if (!trimTablePrefix.equals("")) {
            tableNames = tableNames.stream().filter(tableName -> tableName.startsWith(trimTablePrefix)).collect(Collectors.toList());
        }

        return tableNames;
    }
}
