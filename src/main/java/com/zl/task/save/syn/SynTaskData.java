package com.zl.task.save.syn;


import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.SynTaskVO;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.jdbc.generator.convert.FieldConvert;
import com.zl.utils.other.Ini4jUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @className: com.craw.nd.service.syn-> SynCrawLiveData
 * @description: 同步爬虫 Xhr 文件到百度云
 * @author: zl
 * @createDate: 2024-02-21 13:46
 * @version: 1.0
 * @todo:
 */
public class SynTaskData implements ExecutorTaskService {
    private String srcDir;
    private String desDir;

    public String getDesDir() {
        return desDir;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public void setDesDir(String desDir) {
        this.desDir = desDir;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public static void main(String[] args) throws Exception {
        SynTaskData synTaskData = new SynTaskData();
        synTaskData.synAll();
    }



    @Override
    public void ExecutorTaskService(TaskResource taskResource) {

    }

    @Override
    public void ExecutorTaskService(Object object) {

    }

    @Override
    public void run(TaskVO task) throws Exception {

        synAll();

    }

    public void synAll() throws Exception {
        // 同步昨日到120天的记录
        List<String> tasks = new ArrayList<>();
        tasks.add("list");
        tasks.add("yunTu");
        tasks.add("huiTunLive");
        tasks.add("market");
        tasks.add("weather");
        tasks.add("trendinsight");
        //日期 2023-10-01 -2024-02-17;
        //通过文件日期对不同创建日期进行同步；
        while (true) {
            Date startDate = null;
            Date endDate = null;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < 360; i++) {
                calendar.add(Calendar.DAY_OF_YEAR, -i);
                Date temp = calendar.getTime();
                calendar.setTime(new Date());
                System.out.println(sf.format(temp));
                for (String task : tasks) {
                    System.out.println(task);
                    syn(task, temp, temp);
                }

            }
            System.out.println("已完成本地文件数据同步任务，退出中 ");
            break;
        }
    }

    public void syn(String task, Date startDate, Date endDate) throws Exception {
        // LoggerUtils.logger.info(task+startDate+endDate+"任务开始");
        Ini4jUtils.loadIni("./data/task/syn.ini");
        Ini4jUtils.setSectionValue(task);
        srcDir = Ini4jUtils.readIni("crawSaveDir");
        desDir = Ini4jUtils.readIni("dayDir");
        String computerName = System.getenv("COMPUTERNAME");
        if (computerName == null) {
            // 如果上面的方法不奏效，尝试使用USERNAME环境变量
            computerName = System.getenv("USERNAME");
        }
        Ini4jUtils.setSectionValue("baiduSyncdisk");
        String syncDir = Ini4jUtils.readIni("work");
        Map<String, String> map = null;
        map = new HashMap<>();
        List<String> dirs = DiskIoUtils.getDirListFromDir(srcDir);
        for (String dir : dirs) {
            String value = dir.replace(srcDir, "");
            map.put(value, value);
        }

        List<SynTaskVO> synTaskVOS = new ArrayList<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String s2 = entry.getKey();
            String value = map.get(entry.getKey()).replaceAll("/", "-").replaceAll("&", "-").replaceAll("\\?", "-").replaceAll("_", "-");
            SynTaskVO vo = new SynTaskVO();
            vo.setTaskName(task);
            vo.setStartDate(startDate);
            vo.setEndDate(endDate);
            vo.setHost(computerName);
            vo.setSrcDir(srcDir + FieldConvert.toCameCase(value) + "\\");
            sf.format(vo.getStartDate());
            String s1 = desDir + sf.format(vo.getStartDate()) + "\\" + vo.getTaskName() + "-" + vo.getHost() + "\\" + FieldConvert.toCameCase(value) + "\\";
            vo.setDesDir(s1);
            synTaskVOS.add(vo);
        }
        if (!DiskIoUtils.isExist(syncDir)) {
            DiskIoUtils.createDir(syncDir);
        }
        for (SynTaskVO vo : synTaskVOS) {
            SynchronizationTaskDataUtils.DayDataSynchronization(vo, syncDir);
        }
    }


}
