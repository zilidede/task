package com.zl.task.save.list;

import com.zl.dao.generate.HourLiveRankDO;
import com.zl.dao.generate.HourLiveRankDao;
import com.zl.task.save.parser.ParserFiddlerJson;
import com.zl.task.save.parser.list.SaveHourLiveList;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveHourLiveListTest {

    @Test
    public void parserJson() throws IOException, SQLException {
        SaveHourLiveList saver = new SaveHourLiveList();
        HttpVO httpVO = ParserFiddlerJson.parserXHRJson("./data/test/hourLiveList.txt");
        String url = httpVO.getUrl();
        String json = httpVO.getResponse().getBody();
        saver.parserJson(json, httpVO);

    }

    @Test
    public void save() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
        // 设置时区，例如：系统默认时区
        ZoneId zoneId = ZoneId.systemDefault();

        // 获取当前时间
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        System.out.println("当前时间: " + now.format(formatter));

        // 获取前一个小时的时间
        ZonedDateTime oneHourAgo = now.minusHours(1);
        System.out.println("前一个小时: " + oneHourAgo.format(formatter));

        // 获取前两个小时的时间
        ZonedDateTime twoHoursAgo = now.minusHours(2);
        System.out.println("前两个小时: " + twoHoursAgo.format(formatter));
        String sDir = "S:\\data\\back\\day\\";
        LocalDate currentDate = LocalDate.now();
        String computerName = System.getenv("COMPUTERNAME");
        if (computerName == null) {
            // 如果上面的方法不奏效，尝试使用USERNAME环境变量
            computerName = System.getenv("USERNAME");
        }
        String dir = String.format("%s%s\\list-%s", sDir, currentDate, computerName);
        HourLiveRankDao hourLiveRankDao = new HourLiveRankDao(DefaultDatabaseConnect.getConn());
        List<HourLiveRankDO> hourLiveRankDOS = hourLiveRankDao.findHourLiveRankByCriteria(-1, twoHoursAgo.format(formatter), oneHourAgo.format(formatter), 10000);
        Map<String, Integer> map = new HashMap<>();
        for (HourLiveRankDO hourLiveRankDO : hourLiveRankDOS) {
            map.put(hourLiveRankDO.getRoomId() + "&" + hourLiveRankDO.getAuthorId(), hourLiveRankDO.getWatchCnt());
        }
        List<HourLiveRankDO> volist = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] string = key.split("&");
            HourLiveRankDO vo = new HourLiveRankDO();
            vo.setRoomId(Long.parseLong(string[0]));
            vo.setAuthorId(Long.parseLong(string[1]));
            volist.add(vo);
        }

        if (DiskIoUtils.isExist(dir)) {
            // TODO: 实现保存抖音直播交易榜小时榜的数据;
            SaveHourLiveList saveHourLiveList = new SaveHourLiveList();
            saveHourLiveList.save(dir);
        }
    }
}