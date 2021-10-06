package com.github.tianjing.tgtools.security.sql;

import tgtools.util.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 验证 实体类 的属性中 String 的是否含有 sql 字符
 *
 * @author 田径
 * @date 2021-01-28 16:19
 * @desc
 **/
public abstract class SqlEntityValidHelper {

    /**
     * 敏感字符
     */
    public static char[] SQL_SPECIAL_CHAR = {'\''};
    /**
     * 转义字符
     */
    public static String[] SQL_ESCAPE_STRING = {"''"};
    protected static HashMap<Class, List<Field>> cacheEntityField = new HashMap<>();

    public static List<Field> getEntityFieldFromCache(Object pObject) {
        Class vClass = pObject.getClass();
       // if (cacheEntityField.containsKey(vClass)) {
       //     return cacheEntityField.get(vClass);
       // }

        ArrayList vFields = new ArrayList<>();
        getAllFields(vClass, vFields, String.class);
        cacheEntityField.put(vClass, vFields);

        return vFields;
    }

    public static void setSqlSpecialChar(char[] pSqlSpecialChar) {
        SQL_SPECIAL_CHAR = pSqlSpecialChar;
    }

    public static void setSqlEscapeString(String[] pSqlEscapeString) {
        SQL_ESCAPE_STRING = pSqlEscapeString;
    }

    /**
     * 删除转义字符
     *
     * @param pContent
     * @return
     * @throws SqlEntityValidException
     */
    public static String removeEscapeString(String pContent) throws SqlEntityValidException {
        if (StringUtil.isNullOrEmpty(pContent)) {
            return pContent;
        }

        String vTemp = pContent;
        for (String vEscape : SQL_ESCAPE_STRING) {
            vTemp = StringUtil.replace(vTemp, vEscape, StringUtil.EMPTY_STRING);
        }
        return vTemp;
    }

    /**
     * 验证 是否纯在敏感字符
     *
     * @param pEntity
     * @throws SqlEntityValidException
     */
    public static void validStringFields(Object pEntity) throws SqlEntityValidException {
        List<Field> vFields = getEntityFieldFromCache(pEntity);
        for (Field vField : vFields) {
            try {
                vField.setAccessible(true);
                Object vValue = vField.get(pEntity);
                if (null == vValue || StringUtil.isNullOrEmpty((String) vValue)) {
                    continue;
                }

                String vTemp = removeEscapeString((String) vValue);
                for (char vChar : SQL_SPECIAL_CHAR) {
                    if (vTemp.indexOf(vChar) >= 0) {
                        throw new SqlEntityValidException("验证失败！字段：" + vField.getName() + "存在非法字符；value:" + vValue);
                    }
                }
            } catch (IllegalAccessException e) {

            }
        }
    }

    /**
     * 验证 是否纯在敏感字符
     *
     * @param pContent
     * @throws SqlEntityValidException
     */
    public static void validString(String pContent) throws SqlEntityValidException {
        validString(null, pContent);
    }

    /**
     * 验证 是否纯在敏感字符
     *
     * @param pFieldName
     * @param pContent
     * @throws SqlEntityValidException
     */
    public static void validString(String pFieldName, String pContent) throws SqlEntityValidException {
        String vTemp = removeEscapeString(pContent);
        for (char vChar : SQL_SPECIAL_CHAR) {
            if (vTemp.indexOf(vChar) >= 0) {
                if (!StringUtil.isNullOrEmpty(pFieldName)) {
                    throw new SqlEntityValidException("验证失败！字段：" + pFieldName + "存在非法字符；value:" + pContent);
                }
                throw new SqlEntityValidException("验证失败！存在非法字符；value:" + pContent);
            }
        }
    }

    /**
     * 获取类中所有属性
     *
     * @param pClass
     * @param pFields
     * @param pFieldType
     */
    protected static void getAllFields(Class pClass, List<Field> pFields, Class pFieldType) {
        Field[] vFields = pClass.getDeclaredFields();
        for (Field vField : vFields) {
            if (null == pFieldType || vField.getType().equals(pFieldType)) {
                pFields.add(vField);
            }
        }
        if (pClass.getSuperclass().equals(Object.class)) {
            return;
        }

        getAllFields(pClass.getSuperclass(), pFields, pFieldType);
        return;
    }


}
