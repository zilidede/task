package com.zl.utils.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 磁盘及其文件夹操作
 * @Param:
 * @Auther: zl
 * @Date: 2020/2/13 12:50
 */
public class DiskIoUtils {

    public static boolean createDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return dir.mkdirs();
        } else
            return true;
    }

    public static boolean isDir(String dirPath) {
        File dir = new File(dirPath);

        return dir.isDirectory();
    }

    public static boolean isExist(String path) {
        File f = new File(path);
        return f.exists();
    }

    public static boolean deleteDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists())
            return true;
        else {
            if (dir.isFile())
                dir.delete();
            else
                for (File f : dir.listFiles())
                    deleteDir(f.getPath());

        }
        return dir.delete();
    }

    public static boolean removeDir(String srcDirPath, String desDirPath) {
        File sDir = new File(srcDirPath);
        File dDir = new File(desDirPath);
        String tDirPath = srcDirPath;
        if (!sDir.exists())
            return true;
        if (sDir.isFile()) {
            FileIoUtils.removeFile(sDir.getPath(), dDir.getPath());
        } else {
            desDirPath = dDir.getPath() + srcDirPath.replace(tDirPath, "");
            File dir = new File(desDirPath);
            if (!dir.exists())
                createDir(dir.getPath());
            for (File f : sDir.listFiles()) {
                String desDirPath1 = desDirPath + File.separator + f.getName();
                removeDir(f.getPath(), desDirPath1);
            }
            deleteDir(srcDirPath);

        }
        return false;

    }

    public static boolean copyDir(String srcDirPath, String desDirPath) {
        File sDir = new File(srcDirPath);
        File dDir = new File(desDirPath);
        String tDirPath = srcDirPath;
        if (!sDir.exists())
            return true;
        if (sDir.isFile()) {
            copyFile(sDir.getPath(), dDir.getPath());
        } else {
            desDirPath = dDir.getPath() + srcDirPath.replace(tDirPath, "");
            File dir = new File(desDirPath);
            if (!dir.exists())
                createDir(dir.getPath());
            for (File f : sDir.listFiles()) {
                String desDirPath1 = desDirPath + File.separator + f.getName();
                copyDir(f.getPath(), desDirPath1);
            }
            //DeleteDir(srcDirPath);
        }
        return true;

    }

    /**
     * 获取指定目录下的特定类型文件列表（不包含子目录），并将结果保存到List<String>中。
     *
     * @param dirPath   目录路径
     * @param extension 文件扩展名，"*" 表示所有文件类型
     * @return 包含文件路径字符串的列表
     */
    public static List<String> listFilesByExtension(String dirPath, String extension) {
        Path dir = Paths.get(dirPath);
        List<String> fileList;

        try (Stream<Path> stream = Files.list(dir)) {
            // 如果extension是"*"，则不过滤文件类型；否则，过滤出具有指定扩展名的文件
            fileList = stream
                    .filter(path -> !Files.isDirectory(path)) // 过滤掉所有的目录
                    .filter(path -> "*".equals(extension) || path.toString().toLowerCase().endsWith(extension.toLowerCase()))
                    .map(path -> path.toString()) // 将Path对象转换为字符串表示
                    .collect(Collectors.toList()); // 收集结果到列表
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return List.of(); // 返回一个空列表以处理异常情况
        }

        return fileList;
    }

    public static ArrayList<String> getFileListFromDir(String srcDirPath) {
        //非递归实现文件遍历 使用文件夹queues  获取文件路径列表
        File file = new File(srcDirPath);
        String filename = "";
        ArrayList<String> fileList = new ArrayList<String>();
        if (!file.exists()) {
            return null;

        } else {
            if (file.isFile()) {
                fileList.add(srcDirPath);
                return fileList;
            } else {
                //System.out.println(file.length());
                Queue<String> dirList = new LinkedList<String>();
                dirList.add(srcDirPath);
                String seekDirPath;
                while (!dirList.isEmpty()) {
                    seekDirPath = dirList.poll();
                    file = new File(seekDirPath);
                    for (File f : file.listFiles()) {
                        if (f.isFile()) {
                            fileList.add(f.getAbsolutePath());

                        } else {
                            dirList.add(f.getAbsolutePath());
                        }
                    }
                }

            }
        }
        System.out.println("文件数量：" + fileList.size());
        return fileList;
    }

    public static ArrayList<String> getDirListFromDir(String srcDirPath) {
        //非递归实现文件遍历 使用文件夹queue 获取目录路径列表
        File file = new File(srcDirPath);
        String filename = "";
        ArrayList<String> dirs = new ArrayList<String>();
        if (!file.exists()) {
            return null;

        } else {

            if (file.isFile()) {
                dirs.add(srcDirPath);

            } else {
                //System.out.println(file.length());
                Queue<String> dirList = new LinkedList<String>();
                dirList.add(srcDirPath);
                String seekDirPath;
                while (!dirList.isEmpty()) {
                    seekDirPath = dirList.poll();
                    file = new File(seekDirPath);
                    for (File f : file.listFiles()) {
                        if (f.isFile()) {
                            //fileList.add(f.getAbsolutePath());
                            break;
                        } else {
                            dirList.add(f.getAbsolutePath());
                            dirs.add(f.getAbsolutePath());
                        }
                    }
                }

            }
        }
        System.out.println("目录数量：" + dirs.size());
        return dirs;
    }

    public static boolean copyFile(String oFilePath, String nFilePath) {
        File f = new File(oFilePath);
        String dir = nFilePath.replace(f.getName(), "");
        createDir(dir);
        File f1 = new File(nFilePath);
        if (!f.exists()) {
            return false;
        } else {

            try {
                Files.copy(f.toPath(), f1.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean moveFile(String oFilePath, String nFilePath) {
        File f = new File(oFilePath);
        String dir = nFilePath.replace(f.getName(), "");
        createDir(dir);
        File f1 = new File(nFilePath);
        if (!f.exists()) {
            return false;
        } else {

            try {
                Files.move(f.toPath(), f1.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
