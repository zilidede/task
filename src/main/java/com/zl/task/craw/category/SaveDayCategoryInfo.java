package com.zl.task.craw.category;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.CategoryInfoDO;
import com.zl.dao.generate.CategoryInfoDao;
import com.zl.dao.generate.CategoryTypeDO;
import com.zl.task.impl.SaveService;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.DiskIoUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// 保存抖音日结的类目信息
public class SaveDayCategoryInfo {
    public static void main(String[] args) throws Exception{
        String dir="D:\\data\\task\\爬虫\\compassBrand\\strategyCrowdCrowdCustomSelectUserCount\\运动户外冲锋衣-支付\\";
        saveDayCategoryInfo(dir);
    }
    public static void saveDayCategoryInfo(String dir) throws Exception{
        //sort
        List<String> list= getDayCategoryFilesString(dir);

        // 设置初始日期
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JULY, 10); // 设置为2025年8月10日
        Date recordTime = calendar.getTime();
        List<CategoryInfoDO> list1=new ArrayList<>();

        SaveService saveService=new SaveServiceImpl();
        for(String file:list){
            list1.add(parserDayCategoryInfo(file,recordTime));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            recordTime = calendar.getTime();
        }
        CategoryInfoDao dao=new CategoryInfoDao();
        saveService.savePgSql(dao,list1);
    }
    public static  List<String> getDayCategoryFilesString (String dir) {
        List<String> list = DiskIoUtils.getFileListFromDir(dir);

        // 对文件列表按照文件名中的时间进行排序
        Collections.sort(list, (file1, file2) -> {
            String timeFormat = "yyyy-MM-dd-HH-mm-ss-SSS";
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);

            // 提取文件名部分
            String fileName1 = new File(file1).getName();
            String fileName2 = new File(file2).getName();

            try {
                Date date1 = sdf.parse(fileName1);
                Date date2 = sdf.parse(fileName2);
                return date1.compareTo(date2);
            } catch (ParseException e) {
                throw new RuntimeException("Invalid file name format: " + e.getMessage());
            }
        });

        return list;
    }
    public static CategoryInfoDO parserDayCategoryInfo(String filePath, Date recordTime) throws Exception{
        CategoryInfoDO vo=new CategoryInfoDO();

        HttpVO httpVO= ParserJsonToHttpVO.parserXHRJson(filePath);
        vo.setRecordTime(recordTime);
        vo.setCategoryId(30426);
        JsonParser parser = new JsonParser();
        JsonObject object = null;
        try {
            object = parser.parse(httpVO.getResponse().getBody()).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s=object.get("data").getAsJsonObject().get("crowd_count").getAsString();
        vo.setPayCount(parseStringToNumber(s));
        vo.setGoodsClickCount(0l);
        return vo;
    }
    /**
     * 将带逗号和单位的字符串转换为数字
     * @param str 带逗号和单位的字符串，如 "15,900人"
     * @return 转换后的数字
     */
    public static Long parseStringToNumber(String str) {
        if (str == null || str.isEmpty()) {
            return 0l;
        }

        // 去除所有非数字和非逗号的字符
        String numberStr = str.replaceAll("[^0-9,]", "");

        // 去除逗号
        numberStr = numberStr.replace(",", "");

        try {
            return Long.parseLong(numberStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("无法解析数字: " + str, e);
        }
    }

}
