package com.zl.task.save;

import com.zl.task.craw.keyword.SaveOceanEngineKeyWords;
import com.zl.task.save.db.SaveToPgSql;
import com.zl.task.save.parser.order.SaveDouYinOrder;
import com.zl.task.save.syn.SynTaskData;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.other.Ini4jUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Saver 类负责保存任务产生的所有数据。
 * 包含数据同步模块、数据解析模块和数据保存模块。
 */
public class Saver {
    public static void main(String[] args) throws Exception {
        save();
    }

    /**
     * 主方法，用于启动保存任务。
     * <p>
     * 具体操作包括：
     * - 保存导入订单
     * - 同步数据
     * - 保存近90天的数据（从昨天开始）
     *
     * @throws Exception 如果发生异常
     */
    public static void save() throws Exception {
        syn(); // 同步数据
        SaveToPgSql.cycleSave(3);// 循环保存pqsql

    }

    /**
     * 导入天气数据到数据库。
     *
     * @throws Exception 如果发生异常
     */


    /**
     * 导入每日电商订单表并保存未导入的订单。
     *
     * @throws Exception 如果发生异常
     */
    public static void saveOrder() throws Exception {
        // 加载配置文件
        Ini4jUtils.loadIni("./data/task/syn.ini");
        Ini4jUtils.setSectionValue("order");
        String dir = Ini4jUtils.readIni("unImportDir");
        SaveDouYinOrder saveDouYinOrder = new SaveDouYinOrder();
        saveDouYinOrder.importCsvOrdersToDB(dir); // 导入订单
        saveDouYinOrder.updateOrderUserInfo(dir); // 更新订单用户信息
    }

    /**
     * 同步百度云数据到本地，并将文件压缩包存档后移动到本地同步目录中。
     *
     * @throws Exception 如果发生异常
     */
    public static void syn() throws Exception {
        SynTaskData synTaskData = new SynTaskData();
        synTaskData.synAll();
    }

    public static void saveOceanEngineKeyWords(String srcDir) throws Exception {
        // 保存云图数据
        SaveOceanEngineKeyWords saveOceanEngineSearch = new SaveOceanEngineKeyWords();
        String dir = srcDir + "yunTu-DESKTOP-RBM0GP7\\";
        if (DiskIoUtils.isExist(dir)) {
            saveOceanEngineSearch.setSrcDir(dir);
            saveOceanEngineSearch.save();
        }

    }

    public static void saveCityWeather() throws Exception {
        // 获取配置文件

    }

    /**
     * 根据给定的源目录和日期保存相关数据。
     *
     * @param srcDir 源目录路径
     * @param date   日期字符串（格式：yyyy-MM-dd）
     * @throws Exception 如果发生异常
     */
    public static void save(String srcDir, String date) throws Exception {
        String dir = srcDir + date + "\\";
        // 保存云图数据
        SaveOceanEngineKeyWords saveOceanEngineSearch = new SaveOceanEngineKeyWords();
        if (DiskIoUtils.isExist(dir + "yunTu-ZL\\")) {
            saveOceanEngineSearch.setSrcDir(dir + "yunTu-ZL\\");
            saveOceanEngineSearch.save();
        }
        /*
        // 保存抖音直播交易榜小时榜

        if (DiskIoUtils.isExist(dir + "list-DESKTOP-D5FQ0G0\\")) {
            // TODO: 实现保存抖音直播交易榜小时榜的逻辑
            SaveHourLiveList saveHourLiveList = new SaveHourLiveList();
            saveHourLiveList.save(dir + "list-DESKTOP-D5FQ0G0\\");
        }
        // 保存市场分类数据
        if (DiskIoUtils.isExist(dir + "market-ZL\\")) {
            SaveMarketCategory saver = new SaveMarketCategory();
            saver.save(dir + "market-ZL\\"); //
            SaveMarketCategoryGoods saveGoods = new SaveMarketCategoryGoods();
            saveGoods.save(dir + "market-ZL\\");
        }
        //保存灰豚直播直播数据
        SaveHuiTunAccount saver = new SaveHuiTunAccount();
        if (DiskIoUtils.isExist(dir + "huiTunLive-ZL\\")) {
            saver.save(dir + "huiTunLive-ZL\\"); //
        }

         */

    }
}
