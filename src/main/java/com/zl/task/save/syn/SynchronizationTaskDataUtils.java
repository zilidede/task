package com.zl.task.save.syn;


import cn.hutool.core.io.FileUtil;
import com.zl.task.vo.task.CrawTaskVo;
import com.zl.task.vo.task.SynTaskVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.io.FileZipUtils;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * @className: com.craw.nd.service.other.person.save-> DataSynchronization
 * @description: 数据同步-通过百度云盘
 * @author: zl
 * @createDate: 2024-02-02 12:01
 * @version: 1.0
 * @todo:
 */
public class SynchronizationTaskDataUtils {

    public static void main(String[] args) throws Exception {
    }

    private LinkedList<CrawTaskVo> taskVos;

    public SynchronizationTaskDataUtils(LinkedList<CrawTaskVo> taskVos) throws Exception {


    }

    public void run() throws Exception {

    }

    public static void DayDataSynchronization(SynTaskVO task, String syncDir) throws IOException {
        //
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if (DiskIoUtils.isExist(task.getSrcDir())) {
            List<String> list = DiskIoUtils.getFileListFromDir(task.getSrcDir());
            List<String> backs = new ArrayList<>();
            for (String s : list) {
                FileTime creationTime = Files.readAttributes(Paths.get(s), BasicFileAttributes.class).lastModifiedTime();
                Date attrs = Date.from(creationTime.toInstant());
                String s1 = sf.format(attrs);
                String s2 = sf.format(task.getStartDate());
                if (s1.equals(s2))
                    backs.add(s);
            }
            //移动文件
            if (backs.size() < 1)
                return;

            for (String s : backs) {
                FileIoUtils.removeFile(s, task.getDesDir() + FileIoUtils.getFileName(s));
            }
            System.out.println(task.getSrcDir());
            //压缩文件夹并移动压缩文件到云盘目录
            String[] strings = task.getDesDir().split("\\\\");
            String zipFilePath = "";
            if (strings.length > 1) {
                // 加上当前时间戳
                zipFilePath = syncDir + strings[strings.length - 3] + "-" + strings[strings.length - 2] +  System.currentTimeMillis()+".zip";
            }
            File sourceFolder = new File(task.getDesDir());
            File zipFile = new File(zipFilePath);
            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                FileZipUtils.compressFolder(sourceFolder.getAbsolutePath(), sourceFolder.getName(), zos);
            }

        } else {
            LoggerUtils.logger.warn(task.getTaskName() + "数据同步失败");
        }
    }
}
