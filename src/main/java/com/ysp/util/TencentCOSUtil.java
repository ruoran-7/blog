package com.ysp.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;

import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.util.UUID;

/**
 * 使用到了腾讯云COS,用于图片上传的保存，下面参数均根据自己账号设置
 */
public class TencentCOSUtil {

    private static final String bucketName = "xxxxxxxxxx";

    private static final String SecretId = "xxxxxxxxxxxxxxxx";

    private static final String SecretKey = "xxxxxxxxxxxxxxxxxx";

    private static final String prefix = "xxxxxxxxxxxxxxxxxx";

    public static final String URL = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    private static COSCredentials credentials = new BasicCOSCredentials(SecretId, SecretKey);
    private static ClientConfig clientConfig = new ClientConfig(new Region("ap-xxx"));

    public static String uploadfile(MultipartFile file) {

        COSClient cosClient = new COSClient(credentials, clientConfig);
        String fileName = file.getOriginalFilename();
        try {
            String substring = fileName.substring(fileName.lastIndexOf("."));
            File localFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), substring);
            file.transferTo(localFile);
            fileName = prefix + UUID.randomUUID() + System.currentTimeMillis() + substring;

            PutObjectRequest objectRequest = new PutObjectRequest(bucketName, fileName, localFile);
            cosClient.putObject(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
        return URL + fileName;
    }
}

