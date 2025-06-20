package com.zl.utils.jdbc.generator.bean;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @className: com.craw.nd.common.generator.dao.bean-> ClassReflect
 * @description:
 * @author: zl
 * @createDate: 2023-01-05 10:29
 * @version: 1.0
 * @todo:
 */
public class ClassReflect {
    public static Map<String, ClassBean> getPrivateMembers(Field[] fields, Object commonBean) throws IllegalAccessException {
        Map<String, ClassBean> beans = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            System.out.println(" " + fields[i]);
            Field field = fields[i];
            field.setAccessible(true);
            ClassBean bean = new ClassBean();
            Object object = field.get(commonBean);
            bean.setClassName(field.getName());
            bean.setClassType(field.getType().getTypeName());
            bean.setClassValue(object);
            beans.put(bean.getClassName(), bean);
        }
        return beans;
    }

    public static Map<String, ClassMemberBean> getPublicMembers(Method[] methods) {
        //获取所有公有成员；
        Map<String, ClassMemberBean> map = new HashMap<>();
        for (int i = 0; i < methods.length; i++) {
            int modifiers = methods[i].getModifiers();
            if (Modifier.isPublic(modifiers)) {
                Method method = methods[i];
                System.out.println(method.getName());
                if (method.getName().indexOf("get") >= 0) {
                    String key = method.getName().replace("get", "");
                    key = lowerFirstCase(key);
                    if (!map.containsKey(key)) {
                        ClassMemberBean bean = new ClassMemberBean();
                        System.out.println(" returnType: ");
                        Type returnType = method.getGenericReturnType();// 返回类型
                        System.out.println(" " + returnType);
                        bean.setReturnParameterType(returnType.getTypeName());
                        map.put(key, bean);
                    } else {
                        map.get(key).setReturnParameterType(method.getGenericReturnType().getTypeName());
                    }
                } else if (method.getName().indexOf("set") >= 0) {
                    String key = method.getName().replace("set", "");
                    key = lowerFirstCase(key);
                    Parameter[] parameters = method.getParameters();
                    System.out.println(parameters.length);
                    if (!map.containsKey(key)) {
                        ClassMemberBean bean = new ClassMemberBean();
                        System.out.println(" ParameterType: ");
                        bean.setReturnParameterType(parameters[0].getType().getTypeName());
                        map.put(key, bean);
                    } else {
                        map.get(key).setGetParameterType(parameters[0].getType().getTypeName());
                    }
                }

            }
        }
        return map;
    }

    public static String getShortClassType(String longClassType) {
        //缩减字段类型名称 如 java.lang.Integer = Integer
        if (longClassType.equals("java.lang.Integer")) {
            return "Integer";
        } else if (longClassType.equals("java.lang.String")) {
            return "String";
        } else if (longClassType.equals("java.lang.Long")) {
            return "Long";
        } else if (longClassType.equals("java.lang.Short")) {
            return "Short";
        } else if (longClassType.equals("java.lang.Double")) {
            return "Double";
        } else if (longClassType.equals("java.lang.Float")) {
            return "Float";
        }
        return "";
    }

    public static String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        //首字母小写方法，大写会变成小写，如果小写首字母会消失
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
