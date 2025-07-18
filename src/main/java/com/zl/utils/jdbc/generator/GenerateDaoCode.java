package com.zl.utils.jdbc.generator;


import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.bean.ClassBean;
import com.zl.utils.jdbc.generator.bean.GenerateBean;
import com.zl.utils.jdbc.generator.convert.FieldConvert;
import com.zl.utils.jdbc.generator.jdbc.SimpleJDBC;
import com.zl.utils.jdbc.generator.jdbc.TableInfo;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 代码生成器-dao层代码自动生成
 * @Param:
 * @Auther: zl
 * @Date: 2020/4/20 14:03
 */
public class GenerateDaoCode {
    // 创建一个映射表，将数据类型与其默认初始值关联起来
    private static final Map<String, String> DEFAULT_VALUES = new HashMap<>();

    static {
        DEFAULT_VALUES.put("Integer", "0");
        DEFAULT_VALUES.put("Double", "0.0");
        DEFAULT_VALUES.put("Boolean", "false");
        DEFAULT_VALUES.put("Char", "'\\0'");
        DEFAULT_VALUES.put("Long", "0L");
        DEFAULT_VALUES.put("Float", "0.0f");
        DEFAULT_VALUES.put("Byte", "0");
        DEFAULT_VALUES.put("Short", "0");
        DEFAULT_VALUES.put("String", "\"\"");
        DEFAULT_VALUES.put("List", "new ArrayList<>()");
        DEFAULT_VALUES.put("Map", "new HashMap<>()");
        // 可以继续添加其他类型
    }

    private final String generateFileDir;
    private final String packageName;

    public GenerateDaoCode(String generateFileDir, String packageName) {
        this.generateFileDir = generateFileDir;
        this.packageName = packageName;
    }

    public static void main(String[] args) {
        pgsqlToBean();
    }
    // 将pgsql表结构生成javaBean

    public static void pgsqlToBean() {
        String generateFileDir = "D:\\work\\task\\src\\main\\java\\com\\zl\\dao\\generate";
        String packageName = "com.zl.dao.generate";
        //db-dao&do
        GenerateDaoCode generateDao = new GenerateDaoCode(generateFileDir, packageName);
        SimpleJDBC simpleJDBC = getDefaultJDBC();
        String dataBase = "pgSql";
        String catalog = "public";
        String table = "trendinsight_keywords";
        TableInfo tableInfo = simpleJDBC.getTableInfo(catalog, dataBase, table);
        generateDao.generateJavaBean(table, tableInfo);
        generateDao.codeGeneration(table, tableInfo);
    }

    public static SimpleJDBC getDefaultJDBC() {
        String url = "jdbc:postgresql://127.0.0.1:5432/postgres";
        String className = "org.postgresql.Driver";
        String user = "postgres";
        String password = "332500asd";
        String database = "com/";
        SimpleJDBC simpleJDBC = new SimpleJDBC(className, url, user, password, database);
        return simpleJDBC;
    }

    // 根据数据类型返回初始值
    public String getDefaultValue(String type) {
        String defaultValue = DEFAULT_VALUES.get(type);
        if (defaultValue == null) {
            // 如果类型不在映射表中，返回 null 或抛出异常
            return "null"; // 或者 throw new IllegalArgumentException("Unknown type: " + type);
        }
        return defaultValue;
    }

    public int codeGeneration(String catalog, String dataBase, SimpleJDBC simpleJDBC) {
        //
        //DiskIo.deleteDir(generateFileDir);
        DiskIoUtils.createDir(generateFileDir + "\\back\\");
        DiskIoUtils.createDir(generateFileDir);
        List<String> tableList = simpleJDBC.listAllTables(catalog);
        for (String table : tableList) {
            TableInfo tableInfo = simpleJDBC.getTableInfo(catalog, dataBase, table);
            //generateJavaBean(table, tableInfo);
            // codeGeneration(table, tableInfo);
        }
        return 0;
    }

    public int codeGenerationTable(String tableName, String catalog, String dataBase, SimpleJDBC simpleJDBC) {
        //
        //DiskIo.deleteDir(generateFileDir);
        DiskIoUtils.createDir(generateFileDir + "\\back\\");
        DiskIoUtils.createDir(generateFileDir);
        List<String> tableList = simpleJDBC.listAllTables(catalog);
        for (String table : tableList) {
            if (table.equals(tableName)) {
                TableInfo tableInfo = simpleJDBC.getTableInfo(catalog, dataBase, table);
                codeGeneration(table, tableInfo);
            }
        }
        return 0;
    }

    public int codeGeneration(String tableName, TableInfo tableInfo) {
        String className = FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableName));
        String beanClassName = FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableName + "DO"));
        String sPackage = String.format("package %s;\n", packageName);
        String filePath = String.format("%s\\%sDao.java", generateFileDir, className);
        String backFilePath = String.format("%s\\back\\%sDao.java", generateFileDir, className);
        if (FileIoUtils.fileExists(filePath)) {
            String s = FileIoUtils.readTxtFile(filePath, "utf-8");
            if (s.indexOf("//handle") > 0) {
                DiskIoUtils.removeDir(filePath, backFilePath);
            }
        }
        FileIoUtils.deteleFile(filePath);

        String sImport = "import com.zl.config.Config;\n" +
                "import com.zl.dao.DaoService;\n" +
                "import com.zl.dao.ErrorMsg;\n" +
                "import java.util.List;\n" +
                "import com.zl.utils.jdbc.hikariCP.ConnectionPool;\n"+
                "import java.sql.*;";
        sImport = sImport + String.format("import %s.%s;\n", packageName, beanClassName);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sNow = dateFormat.format(now);
        String sAnnotation = String.format("/**\n" +
                " * @Description:%s\n" +
                " * @Param:%s\n" +
                " * @Auther: %s\n" +
                " * @Date: %s\n" +
                " */\n", "", "", "zl", sNow);
        String sClassName = String.format("public class %s implements DaoService<%s>{\n", className + "Dao", beanClassName);
        String statementCode =
                        "    private ErrorMsg errorMsg;\n";

        String sFunctions = String.format("\n" +
                "    public %s() throws SQLException {\n" +
                "        errorMsg=new ErrorMsg();\n" +
                "    }\r\n", className + "Dao");
        String temp = "    public ErrorMsg getErrorMsg() {\n" +
                "        return errorMsg;\n" +
                "    }\n";
        temp = temp + generateDoInsert(tableInfo);
        sFunctions = sFunctions + temp;
        temp = generateDoUpdate(tableInfo);
        sFunctions = sFunctions + temp;
        temp = generateDoDelete(tableInfo);
        sFunctions = sFunctions + temp;
        temp = generateDoBatch(tableInfo);
        sFunctions = sFunctions + temp;

        //findFields
        for (Map.Entry<String, String> entry : tableInfo.getFieldsMap().entrySet()) {
            String key = entry.getKey();
            if (!key.equals(tableInfo.getPrimaryKey())) {
                //temp = generateFind(tableInfo.getTableName(), key, entry.getValue(), tableInfo.getPrimaryKey(), tableInfo.getFieldsMap().get(tableInfo.getPrimaryKey()));
                //sFunctions = sFunctions + temp;
            }
            temp = "";
        }

        String content = sPackage + sImport + sAnnotation + sClassName + statementCode + sFunctions +
                "   \n" +
                "}\n";
        FileIoUtils.writeTxtFile(filePath, content, "utf-8");
        return 0;
    }

    public void generateJavaBean(String tableName, TableInfo tableInfo) {
        String className = FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableName + "DO"));
        String javaFilePath = generateFileDir + "\\" + className + ".java";
        FileIoUtils.deteleFile(javaFilePath);
        Map<String, String> memberMap = new HashMap();
        for (Map.Entry<String, String> entry : tableInfo.getFieldsMap().entrySet()) {
            memberMap.put(FieldConvert.toCameCase(entry.getKey()), entry.getValue());
        }

        GenerateBean obj = new GenerateBean(javaFilePath, className, packageName);
        Map<String, ClassBean> memberMap1 = new HashMap<>();
        for (Map.Entry<String, String> entry : memberMap.entrySet()) {
            ClassBean classBean = new ClassBean();
            classBean.setClassName(entry.getKey());
            classBean.setClassType(entry.getValue());
            classBean.setClassValue(getDefaultValue(entry.getValue()));
            memberMap1.put(entry.getKey(), classBean);
        }
        obj.getBeanType().setMemberMap(memberMap1);
        obj.setParameterInitFlag(true);
        obj.codeGeneration();
    }

    private String generateDoInsert(TableInfo tableInfo) {
        String beanClassName = FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableInfo.getTableName() + "DO"));
        String sFunctions = "";
        String[] fields = new String[tableInfo.getFieldsMap().size()];
        int i = 0;
        int fieldCount = fields.length;
        for (Map.Entry<String, String> entry : tableInfo.getFieldsMap().entrySet()) {
            fields[i++] = entry.getKey();
        }
        String sql = String.format("insert into %s(", tableInfo.getTableName());
        String values = "values (?";
        LinkedHashMap<String, String> tempMap = new LinkedHashMap<>();
        for (int j = 0; j < fieldCount; j++) {
            sql = sql + fields[j];
            if (j == fieldCount - 1) {
                sql = sql + ')';
                values = values + ")";
            } else {
                sql = sql + ',';
                values = values + ",?";
            }

            tempMap.put(FieldConvert.toCameCase(fields[j]), tableInfo.getFieldsMap().get(fields[j]));
        }
        sql = sql + values;
        sql = String.format("        String sql = \"%s\";", sql);
        String temp = String.format("@Override\n" + "    public void doInsert(%s vo) throws SQLException {\n" +
                "%s\n" +
                "        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {\n" +
                "%s" +
                "        if(pStmt.execute())\n" +
                "            errorMsg.setCode(0);\n" +
                "        else{\n" +
                "            errorMsg.setCode(Config.FAIL_INSERT);\n" +
                "            errorMsg.setMsg(pStmt.toString());\n" +
                "        }\n" +
                " } catch (SQLException e) {\n" +
                "            // 异常处理\n" +
                "        }"+
                "    }", beanClassName, sql, generateJdbcCode(tempMap));

        sFunctions = sFunctions + temp;
        return sFunctions;
    }

    private String generateDoUpdate(TableInfo tableInfo) {
        String beanClassName = FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableInfo.getTableName() + "DO"));
        String sFunctions = "";
        String[] fields = new String[tableInfo.getFieldsMap().size()];
        int i = 0;
        int fieldCount = fields.length;
        for (Map.Entry<String, String> entry : tableInfo.getFieldsMap().entrySet()) {
            fields[i++] = entry.getKey();
        }
        String sql = String.format("update  %s SET ", tableInfo.getTableName());
        LinkedHashMap<String, String> tempMap = new LinkedHashMap<>();
        for (int j = 0; j < fieldCount; j++) {
            if (j == fieldCount - 1) {
                sql = sql + String.format(" %s=?", fields[j]);

            } else
                sql = sql + String.format(" %s=? ,", fields[j]);

            tempMap.put(FieldConvert.toCameCase(fields[j]), tableInfo.getFieldsMap().get(fields[j]));
        }
        sql = sql + String.format("WHERE %s=?", tableInfo.getPrimaryKey());
        String s = generateJdbcCode(tempMap.size() + 1, FieldConvert.toCameCase(tableInfo.getPrimaryKey()), tableInfo.getFieldsMap().get(tableInfo.getPrimaryKey()));
        sql = String.format("        String sql = \"%s\";", sql);
        /*
        String temp = String.format("\n" + "    public void doUpdate(%s vo) throws SQLException {\n" +
                "%s\n" +
                "        pStmt = conn.prepareStatement(sql);\n" +
                "%s" +
                "        if(pStmt.execute())\n" +
                "            errorMsg.setCode(0);\n" +
                "        else{\n" +
                "            errorMsg.setCode(Config.FAIL_INSERT);\n" +
                "            errorMsg.setMsg(pStmt.toString());\n" +
                "        }\n" +
                "    }", beanClassName, sql, generateJdbcCode(tempMap) + s);

         */

        String temp = String.format("@Override\n" + "     public void doUpdate(%s vo) throws SQLException {\n" +

                "    }", beanClassName);

        sFunctions = sFunctions + temp;
        return sFunctions;

    }

    private String generateDoDelete(TableInfo tableInfo) {
        String beanClassName = FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableInfo.getTableName() + "DO"));
        String sFunctions = "";
        String sql = String.format("DELETE FROM  %s WHERE %s=?", tableInfo.getTableName(), tableInfo.getPrimaryKey());
        sql = String.format("        String sql =String.format(\"%s\",vo.get%s());", sql, FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableInfo.getPrimaryKey())));
        /*
        String temp = String.format("@Override\n" + "    public void doDelete(%s vo) throws SQLException {\n" +
                "%s\n" +
                "        pStmt = conn.prepareStatement(sql);\n" +
                "        if(pStmt.execute())\n" +
                "            errorMsg.setCode(0);\n" +
                "        else{\n" +
                "            errorMsg.setCode(Config.FAIL_INSERT);\n" +
                "            errorMsg.setMsg(pStmt.toString());\n" +
                "        }\n" +
                "    }", beanClassName, sql);

         */
        String temp = String.format("@Override\n" + "    public void doDelete(%s vo) throws SQLException {\n" +

                "        }\n" , beanClassName);
        sFunctions = sFunctions + temp;
        return sFunctions;
    }

    private String generateDoBatch(TableInfo tableInfo) {
        String beanClassName = FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableInfo.getTableName() + "DO"));
        String sFunctions = "";
        String sql = String.format("DELETE FROM  %s WHERE %s=?", tableInfo.getTableName(), tableInfo.getPrimaryKey());
        sql = String.format("        String sql =String.format(\"%s\",vo.get%s());", sql, FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableInfo.getPrimaryKey())));
        /*
        String temp = String.format("@Override\n" + "    public void doDelete(%s vo) throws SQLException {\n" +
                "%s\n" +
                "        pStmt = conn.prepareStatement(sql);\n" +
                "        if(pStmt.execute())\n" +
                "            errorMsg.setCode(0);\n" +
                "        else{\n" +
                "            errorMsg.setCode(Config.FAIL_INSERT);\n" +
                "            errorMsg.setMsg(pStmt.toString());\n" +
                "        }\n" +
                "    }", beanClassName, sql);

         */
        String temp = String.format("@Override\n" + "    public void doBatch(List<%s> vo) throws SQLException {\n" +

                "        }\n" , beanClassName);
        sFunctions = sFunctions + temp;
        return sFunctions;
    }

    private String generateFind(String tableName, String field, String fieldType, String primaryKey, String primaryKeyType) {
        String beanClassName = FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(tableName + "DO"));
        String sFunctions = "";
        String sql = String.format("select %s from %s  where %s='%ss'", field, tableName, primaryKey, "%");
        sql = String.format("String sql =String.format(\"%s\",vo.get%s());", sql, FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(primaryKey)));
        String temp = String.format("\n" + "    public %s find%s(%s  vo) throws SQLException {\n" +
                "            %s result = null;\n", fieldType, FieldConvert.toFirstCharUppercase(FieldConvert.toCameCase(field)), beanClassName, fieldType);
        temp = temp + "           " + sql;
        temp = temp + String.format("\n" +
                "            pStmt = conn.prepareStatement(sql);\n" +
                "            ResultSet set = pStmt.executeQuery();\n" +
                "            set.next();\n" +
                "            if(set.getRow()==1)\n" +
                "%s\n" +
                "            return result;\n" +
                "        }", generateResultSetStatement(field, fieldType));

        sFunctions = sFunctions + temp;
        return sFunctions;
    }

    private String generateJdbcCode(HashMap<String, String> fieldsMap) {
        String sPStmt = "";
        int i = 1;
        for (Map.Entry<String, String> entry : fieldsMap.entrySet()) {
            sPStmt = sPStmt + generateJdbcCode(i++, entry.getKey(), entry.getValue());
        }
        return sPStmt;

    }

    private String generateJdbcCode(int parameterIndex, String variableName, String variableType) {
        if (variableType == null)
            return "";
        String code;
        switch (variableType) {
            case "Boolean":
                code = String.format("        pStmt.setBoolean(%d, vo.get%s());\n", parameterIndex, FieldConvert.toFirstCharUppercase(variableName));
                break;
            case "byte []":
                code = String.format("        pStmt.setBytes(%d, vo.get%s());\n", parameterIndex, FieldConvert.toFirstCharUppercase(variableName));
                break;
            case "String":
                code = String.format("        pStmt.setString(%d, vo.get%s());\n", parameterIndex, FieldConvert.toFirstCharUppercase(variableName));
                break;
            case "Integer":
                code = String.format("        pStmt.setInt(%d, vo.get%s());\n", parameterIndex, FieldConvert.toFirstCharUppercase(variableName));
                break;
            case "Long":
                code = String.format("        pStmt.setLong(%d, vo.get%s());\n", parameterIndex, FieldConvert.toFirstCharUppercase(variableName));
                break;
            case "Float":
                code = String.format("        pStmt.setFloat(%d, vo.get%s());\n", parameterIndex, FieldConvert.toFirstCharUppercase(variableName));
                break;
            case "Double":
                code = String.format("        pStmt.setDouble(%d, vo.get%s());\n", parameterIndex, FieldConvert.toFirstCharUppercase(variableName));
                break;
            case "Date": {
                String s = String.format("Timestamp ts = new Timestamp(vo.get%s().getTime());\n"
                        , FieldConvert.toFirstCharUppercase(variableName));

                code = s + String.format("        pStmt.setTimestamp(%d, ts);\n", parameterIndex);
                break;
            }
            default:
                code = "";
                break;
        }
        return code;
    }

    private String generateResultSetStatement(String variableName, String variableType) {
        if (variableType == null)
            return "";
        String code;
        switch (variableType) {
            case "byte []":
                code = String.format("                result = set.getBytes(\"%s\"); ", variableName);
                break;
            case "String":
                code = String.format("                result = set.getString(\"%s\"); ", variableName);
                break;
            case "Integer":
                code = String.format("                result = set.getInt(\"%s\"); ", variableName);
                break;
            case "Long":
                code = String.format("                result = set.getLong(\"%s\"); ", variableName);
                break;
            case "Float":
                code = String.format("                result = set.getFloat(\"%s\"); ", variableName);
                break;
            case "Double":
                code = String.format("                result = set.getDouble(\"%s\"); ", variableName);
                break;
            case "Date":
                code = String.format("                result = set.getDate(\"%s\"); ", variableName);
                break;
            default:
                code = "";
                break;
        }
        return code;
    }

}
