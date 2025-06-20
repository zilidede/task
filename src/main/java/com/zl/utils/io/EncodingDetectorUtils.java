package com.zl.utils.io;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.zl.utils.log.LoggerUtils;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EncodingDetectorUtils {

    public static String detectEncoding(File filePath) throws IOException {
        byte[] buf = new byte[4096];
        String encoding;
        UniversalDetector detector = new UniversalDetector(null);
        try (InputStream inputStream = new FileInputStream(filePath)) {
            int nread;
            while ((nread = inputStream.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            encoding = detector.getDetectedCharset();
        }

        detector.reset();
        if (encoding == null) {
            // 默认编码，当无法检测时使用
            encoding = "utf-8"; // 或者其他的默认值
        }

        return encoding;
    }

    public static String DetectFileEncodingWithTika(String filePath) throws IOException {
        String encoding = "utf-8";
        // 先将文件读入字节数组
        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

        // 使用 ByteArrayInputStream 包装字节数组，因为它支持 mark 和 reset
        try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
            CharsetDetector detector = new CharsetDetector();
            detector.setText(inputStream);

            CharsetMatch charsetMatch = detector.detect();

            if (charsetMatch != null) {
                encoding = charsetMatch.getName();
                // System.out.println("Detected encoding: " + charsetMatch.getName());
                // System.out.println("Confidence: " + charsetMatch.getConfidence() + "%");
            } else {
                LoggerUtils.logger.warn("解析%s文件格式出错", filePath);
                //System.out.println("Encoding detection failed.");
            }
        }
        return encoding;
    }


    public static void main(String[] args) throws IOException {
        File file = new File("S:\\data\\back\\day\\2025-02-21\\market-ZL\\compassApiShopProductProductChanceMarketCategoryOverviewPriceAnalysisProduct\\2025-02-21 03-40-53-618.txt");
        String encoding = detectEncoding(file);
        System.out.println("Detected encoding: " + encoding);
    }
}
