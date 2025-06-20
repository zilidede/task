package com.zl.utils.jdbc.generator.bean;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.convert.FieldConvert;
import com.zl.utils.jdbc.generator.jdbc.SimpleJDBC;
import com.zl.utils.jdbc.generator.jdbc.TableInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 生成javaBean;
 * @Param:
 * @Auther: zl
 * @Date: 2020/4/21 01:00
 */
public class GenerateBean {

    private BeanType beanType;
    private Boolean parameterInitFlag = false;//参数初始化标识；

    public GenerateBean(String programDir, String className, String packageName) {

        beanType = new BeanType();
        beanType.setClassName(className);
        beanType.setPackageName(packageName);

        String temp = packageName.replaceAll("\\.", "\\\\");
        programDir = programDir.replace(temp, "");
        programDir = programDir.replace(className, "");
        programDir = programDir.replace(".java", "");
        String javaFilePath = String.format("%s\\%s\\%s.java", programDir, temp, className);
        beanType.setBeanFilePath(javaFilePath);
    }

    //导入字段类型
    public void importBeanMembersFromMap(Map<String, ClassBean> memberMap) {
        //导入bean格式
        beanType.setMemberMap(memberMap);
    }

    public void importBeanMembersFromJson(String json) {
        //导入bean格式 json -排除异常字段 {} null []
        HashMap<String, ClassBean> memberMap = new HashMap<>();
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        Set<String> keys = object.keySet();
        for (String key : keys) {
            ClassBean bean = new ClassBean();
            String key1 = FieldConvert.toCameCase(key);
            bean.setClassName(key1);
            bean.setClassValue(object.get(key));
            if (!object.get(key).toString().equals("null")) {
                //

                bean.setClassType(GeneratorConvert.getJsonFieldType(object.get(key)));
                memberMap.put(bean.getClassName(), bean);
            }
        }
        beanType.setMemberMap(memberMap);
    }

    public void importBeanMembersFromExcel(String ExcelFilePath) {
        //导入bean格式 excel
        HashMap<String, ClassBean> memberMap = new HashMap<>();
        beanType.setMemberMap(memberMap);
    }

    public void importBeanMembersFromJDBC(SimpleJDBC simpleJDBC, String catalog, String dataBase, String tableName) {
        //导入bean格式 jdbc
        TableInfo tableInfo = simpleJDBC.getTableInfo(catalog, dataBase, tableName);
        HashMap<String, ClassBean> memberMap = new HashMap<>();
        for (Map.Entry<String, String> entry : tableInfo.getFieldsMap().entrySet()) {
            ClassBean classBean = new ClassBean();
            classBean.setClassType(entry.getValue());
            classBean.setClassName(FieldConvert.toCameCase(entry.getKey()));
            memberMap.put(classBean.getClassName(), classBean);
        }
        beanType.setMemberMap(memberMap);
    }

    public void setClassName(String className) {
        beanType.setClassName(className);
    }

    public BeanType getBeanType() {
        return beanType;
    }

    public void setBeanType(BeanType beanType) {
        this.beanType = beanType;
    }

    public Boolean getParameterInitFlag() {
        return parameterInitFlag;
    }

    public void setParameterInitFlag(Boolean parameterInitFlag) {
        this.parameterInitFlag = parameterInitFlag;
    }

    public int codeGeneration() {
        String sPackage = String.format("package %s;\n", beanType.getPackageName());
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sNow = dateFormat.format(now);
        String sAnnotation = String.format("/**\n" +
                " * @Description:%s\n" +
                " * @Param:%s\n" +
                " * @Auther: %s\n" +
                " * @Date: %s\n" +
                " */\n", "", "", "zl", sNow);
        String sImport = "import java.util.*;\n";
        String sClassName = String.format("public class %s {\n", beanType.getClassName());
        String statementCode = "";

        for (Map.Entry<String, ClassBean> entry : beanType.getMemberMap().entrySet()) {
            String key = entry.getKey();
            String value = "";
            try {
                value = entry.getValue().getClassType();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            String temp = "";
            if (!parameterInitFlag) {

                temp = String.format("   private %s %s;\n", value, key);
            } else {
                temp = String.format("   private %s %s=%s", value, key, entry.getValue().getClassValue());
                temp = temp + ";\n";
            }
            statementCode = statementCode + temp;

        }

        String sFunctions = "";
        for (Map.Entry<String, ClassBean> entry : beanType.getMemberMap().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getClassType();
            String upperValue = FieldConvert.toFirstCharUppercase(key);
            String temp = String.format(
                    "   public %s get%s() {\n" +
                            "      return %s;\n" +
                            "   }", value, upperValue, key);
            temp = temp + String.format("\n" +
                    "   public void set%s(%s %s) {\n" +
                    "      this.%s = %s;\n" +
                    "   }\n", upperValue, value, key, key, key);
            sFunctions = sFunctions + temp;
        }
        String content = sPackage + sImport + sAnnotation + sClassName + statementCode + sFunctions +
                "   \n" +
                "}\n";
        if (FileIoUtils.fileExists(beanType.getBeanFilePath())) {
            FileIoUtils.deteleFile(beanType.getBeanFilePath());
            FileIoUtils.createFile(beanType.getBeanFilePath());
        }

        FileIoUtils.writeTxtFile(beanType.getBeanFilePath(), content, "utf-8");
        return 0;
    }

    private String getDataStructureInitString(String dataStructure) {
        //获取不同基本数据结构的初始化字符串 比如 输入string 返回"";
        return "0";
    }
}
