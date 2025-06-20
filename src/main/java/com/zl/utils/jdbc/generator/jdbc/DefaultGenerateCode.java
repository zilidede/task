package com.zl.utils.jdbc.generator.jdbc;


import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.GenerateDaoCode;
import com.zl.utils.jdbc.generator.bean.*;
import com.zl.utils.jdbc.generator.convert.FieldConvert;
import com.zl.utils.jdbc.generator.convert.JavaTypeToJDBC;
import com.zl.utils.jdbc.generator.dao.GenerateDBTable;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @className: com.craw.nd.common.generator-> GenerateCode
 * @description: 通过将数据库结构化文件生成bean
 * @author: zl
 * @createDate: 2023-02-11 14:56
 * @version: 1.0
 * @todo:
 */
public class DefaultGenerateCode<T> {
    String generateFileDir = "C:\\work\\craw\\src\\main\\java\\com\\craw\\nd\\dao\\generate";
    String packageName = "com.craw.nd.dao.generate";

    //bean相关；结构化文件<->bean;
    //
    public void generatorDaoFromClassName(String className) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        // bean-table-dao;
        generateDbFromBean(className);
        String[] strings = className.split(".");
        String tableName = strings[strings.length - 1];
        generateDaoFromPgDB("hui_tun_live_flow");
    }

    public void generateBeanFromFile(ClassBeanType beanType) {
        GenerateBean generator = new GenerateBean(beanType.getProgramDir(), beanType.getClassName(), beanType.getPackageName());
        String fileType = FileIoUtils.getFileExtension(new File(beanType.getFilePath()));
        beanType.setFileContent(FileIoUtils.readTxtFile(beanType.getFilePath(), "utf-8"));
        if (fileType.equals("json")) {
            generator.importBeanMembersFromJson(beanType.getFileContent());
        } else if (fileType.equals("xlsx")) {
            generator.importBeanMembersFromExcel(beanType.getFileContent());
        } else if (fileType.equals("xml")) {
            generator.importBeanMembersFromJson(beanType.getFileContent());
        }
        generator.setParameterInitFlag(true);
        generator.codeGeneration();
    }

    public void generateFileFromBean() {


    }

    public void generateBeanFromDb(String className, String tableName) {
        ClassBeanType beanType = getClassBeanType(className);
        DBType dbType = getDBType(tableName);
        GenerateBean generator = new GenerateBean(beanType.getProgramDir(), beanType.getClassName(), beanType.getPackageName());
        generator.importBeanMembersFromJDBC(dbType.getSimpleJDBC(), dbType.getCatalog(), dbType.getDataBase(), dbType.getTableName());
        generator.setParameterInitFlag(true);
        generator.codeGeneration();
    }

    public void generateDbFromBean(String className) throws ClassNotFoundException, IllegalAccessException, SQLException, InstantiationException {
        List<DBFieldBean> fields = new ArrayList<>();
        Class<?> c = null;
        c = Class.forName(className);
        Field[] field1s = c.getDeclaredFields();
        Object t = c.newInstance();
        Map<String, ClassBean> beanPriMap = ClassReflect.getPrivateMembers(field1s, t);
        for (Map.Entry<String, ClassBean> entry : beanPriMap.entrySet()) {
            DBFieldBean field = new DBFieldBean();
            String key = FieldConvert.toField(entry.getValue().getClassName());
            key = key.replace("\u007F", "");
            String value = JavaTypeToJDBC.javaTpPgSql(FieldConvert.getShortClassType(entry.getValue().getClassType()));
            field.setField(key);
            field.setType(value);
            fields.add(field);
        }
        GenerateDBTable obj = new GenerateDBTable(DefaultDatabaseConnect.getPGJDBC());
        String s = FieldConvert.toField(className.substring(0, className.length() - 2));
        String[] s1 = s.split("\\.");
        s = s1[s1.length - 1];
        s = s.substring(1);
        obj.GeneratePgSqlTable(fields, s);
    }


    //dao-bean;
    public void generateDaoFromPgDB(String table) {
        //db-dao
        GenerateDaoCode generateDao = new GenerateDaoCode(generateFileDir, packageName);
        SimpleJDBC simpleJDBC = DefaultDatabaseConnect.getPGJDBC();
        String dataBase = "pgSql";
        String catalog = "public";
        TableInfo tableInfo = simpleJDBC.getTableInfo(catalog, dataBase, table);
        generateDao.codeGeneration(table, tableInfo);
    }


    public ClassBeanType getClassBeanType(String className) {
        ClassBeanType beanType = new ClassBeanType();
        String javaFilePath = "C:\\work\\craw\\src\\main\\java";
        String packageName = "com.craw.nd.dao.generate";
        beanType.setPackageName(packageName);
        beanType.setProgramDir(javaFilePath);
        beanType.setClassName(className);
        return beanType;
    }

    public DBType getDBType(String tableName) {
        DBType dbType = new DBType();
        dbType.setCatalog("public");
        dbType.setDataBase("pgSql");
        //  dbType.setSimpleJDBC(DefaultDatabaseConnect.getPGJDBC());
        dbType.setTableName(tableName);
        return dbType;
    }

    public Boolean isExistDbTable(String tableName) {
        //数据库是否存在
        return false;
    }
}
