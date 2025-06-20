package com.zl.task.craw.keyword;
//解密 巨量算数 xhr 密文

import com.google.gson.JsonParser;
import com.zl.task.save.parser.ParserFiddlerJson;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CypTrendinsightData {

    public static void main(String[] args) throws Exception {
        String data = "";
        String filePath = "./data/test/trendinsight.txt";
        JsonParser parser = new JsonParser();
        System.out.println("解析关键词的解=index/get_multi_keyword_interpretation");
        filePath = "S:\\data\\task\\爬虫\\trendinsight\\indexGetMultiKeywordInterpretation\\trendinsight.txt";
        data = parser.parse(ParserFiddlerJson.parserXHRJson(filePath).getResponse().getBody()).getAsJsonObject().get("data").getAsString();
        String decryptedString = CypTrendinsightData.decrypt(data);
        System.out.println(decryptedString);
        System.out.println("解析关键词的热门趋势=index/get_multi_keyword_hot_trend");
        filePath = "S:\\data\\task\\爬虫\\trendinsight\\indexGetMultiKeywordHotTrend\\trendinsight.txt";
        data = parser.parse(ParserFiddlerJson.parserXHRJson(filePath).getResponse().getBody()).getAsJsonObject().get("data").getAsString();
        decryptedString = CypTrendinsightData.decrypt(data);
        System.out.println(decryptedString);
        System.out.println("解析关键词的关联词=index/get_relation_word");
        filePath = "S:\\data\\task\\爬虫\\trendinsight\\indexGetRelationWord\\trendinsight.txt";
        data = parser.parse(ParserFiddlerJson.parserXHRJson(filePath).getResponse().getBody()).getAsJsonObject().get("data").getAsString();
        decryptedString = CypTrendinsightData.decrypt(data);
        System.out.println(decryptedString);
        System.out.println("解析获取关键词的画像=index/get_portrait");
        filePath = "S:\\data\\task\\爬虫\\trendinsight\\indexGetPortrait\\trendinsight.txt";
        data = parser.parse(ParserFiddlerJson.parserXHRJson(filePath).getResponse().getBody()).getAsJsonObject().get("data").getAsString();
        decryptedString = CypTrendinsightData.decrypt(data);
        System.out.println(decryptedString);
    }

    public static String decrypt(String data) throws Exception {
        String keyBase64 = "SjXbYTJb7zXoUToSicUL3A==";
        String ivBase64 = "OekMLjghRg8vlX/PemLc+Q==";

        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        byte[] ivBytes = Base64.getDecoder().decode(ivBase64);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
        String decryptedString = new String(decryptedBytes, StandardCharsets.UTF_8);
        // System.out.println(decryptedString);
        return decryptedString;
    }
}
