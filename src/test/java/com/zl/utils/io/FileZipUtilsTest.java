package com.zl.utils.io;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * 测试类用于验证 FileZipUtils.compressFolder 方法的功能。
 * 该方法负责将指定文件夹及其内容压缩到一个 ZIP 输出流中。
 */
public class FileZipUtilsTest {

    /**
     * 使用 TemporaryFolder 规则来创建临时文件和文件夹，
     * 确保在测试结束后自动清理这些资源。
     */
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * 源文件夹，用于存放测试文件和子文件夹。
     */
    private File sourceFolder;

    /**
     * 在每个测试方法执行之前初始化源文件夹。
     *
     * @throws IOException 如果文件操作失败
     */
    @Before
    public void setUp() throws IOException {
        sourceFolder = temporaryFolder.newFolder("sourceFolder");
    }

    /**
     * 测试空文件夹的情况。
     * 预期结果：生成的 ZIP 文件应为空。
     *
     * @throws IOException 如果文件操作失败
     */
    @Test
    public void compressFolder_EmptyFolder_EmptyZip() throws IOException {
        // 创建一个空的 ZIP 文件
        File zipFile = temporaryFolder.newFile("emptyFolder.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            FileZipUtils.compressFolder(sourceFolder.getAbsolutePath(), sourceFolder.getName(), zos);
        }

        // 验证 ZIP 文件是否为空
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry = zis.getNextEntry();
            assertTrue(entry == null); // 确保ZIP文件为空
        }
    }

    /**
     * 测试仅包含文件的文件夹。
     * 预期结果：所有文件都应被正确添加到 ZIP 文件中。
     *
     * @throws IOException 如果文件操作失败
     */
    @Test
    public void compressFolder_FolderWithFiles_FilesInZip() throws IOException {
        // 创建两个测试文件
        File file1 = new File(sourceFolder, "file1.txt");
        File file2 = new File(sourceFolder, "file2.txt");
        file1.createNewFile();
        file2.createNewFile();

        // 压缩文件夹到 ZIP 文件
        File zipFile = temporaryFolder.newFile("folderWithFiles.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            FileZipUtils.compressFolder(sourceFolder.getAbsolutePath(), sourceFolder.getName(), zos);
        }

        // 验证 ZIP 文件中的条目
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry = zis.getNextEntry();
            assertEquals("sourceFolder/file1.txt", entry.getName());
            entry = zis.getNextEntry();
            assertEquals("sourceFolder/file2.txt", entry.getName());
            entry = zis.getNextEntry();
            assertTrue(entry == null); // 确保没有额外的条目
        }
    }

    /**
     * 测试包含子文件夹的文件夹。
     * 预期结果：子文件夹及其内容应被正确处理并添加到 ZIP 文件中。
     *
     * @throws IOException 如果文件操作失败
     */
    @Test
    public void compressFolder_FolderWithSubfolders_SubfoldersInZip() throws IOException {
        // 创建一个子文件夹和一个测试文件
        File subFolder = new File(sourceFolder, "subFolder");
        subFolder.mkdir();
        File file1 = new File(subFolder, "file1.txt");
        file1.createNewFile();

        // 压缩文件夹到 ZIP 文件
        File zipFile = temporaryFolder.newFile("folderWithSubfolders.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            FileZipUtils.compressFolder(sourceFolder.getAbsolutePath(), sourceFolder.getName(), zos);
        }

        // 验证 ZIP 文件中的条目
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry = zis.getNextEntry();
            assertEquals("sourceFolder/subFolder/file1.txt", entry.getName());
            entry = zis.getNextEntry();
            assertTrue(entry == null); // 确保没有额外的条目
        }
    }

    /**
     * 测试包含空子文件夹的文件夹。
     * 预期结果：空子文件夹应被正确处理，但不会在 ZIP 文件中创建空条目。
     *
     * @throws IOException 如果文件操作失败
     */
    @Test
    public void compressFolder_FolderWithEmptySubfolder_EmptySubfolderInZip() throws IOException {
        // 创建一个空的子文件夹
        File subFolder = new File(sourceFolder, "emptySubFolder");
        subFolder.mkdir();

        // 压缩文件夹到 ZIP 文件
        File zipFile = temporaryFolder.newFile("folderWithEmptySubfolder.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            FileZipUtils.compressFolder(sourceFolder.getAbsolutePath(), sourceFolder.getName(), zos);
        }

        // 验证 ZIP 文件是否为空
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry = zis.getNextEntry();
            assertTrue(entry == null); // 确保ZIP文件为空
        }
    }

    /**
     * 测试包含文件和子文件夹的文件夹。
     * 预期结果：所有文件和子文件夹的内容都应被正确压缩到 ZIP 文件中。
     *
     * @throws IOException 如果文件操作失败
     */
    @Test
    public void compressFolder_FolderWithFilesAndSubfolders_AllInZip() throws IOException {
        // 创建一个测试文件和一个子文件夹及其中的测试文件
        File file1 = new File(sourceFolder, "file1.txt");
        file1.createNewFile();

        File subFolder = new File(sourceFolder, "subFolder");
        subFolder.mkdir();
        File file2 = new File(subFolder, "file2.txt");
        file2.createNewFile();

        // 压缩文件夹到 ZIP 文件
        File zipFile = temporaryFolder.newFile("folderWithFilesAndSubfolders.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            FileZipUtils.compressFolder(sourceFolder.getAbsolutePath(), sourceFolder.getName(), zos);
        }

        // 验证 ZIP 文件中的条目
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry = zis.getNextEntry();
            assertEquals("sourceFolder/file1.txt", entry.getName());
            entry = zis.getNextEntry();
            assertEquals("sourceFolder/subFolder/file2.txt", entry.getName());
            entry = zis.getNextEntry();
            assertTrue(entry == null); // 确保没有额外的条目
        }
    }
}

