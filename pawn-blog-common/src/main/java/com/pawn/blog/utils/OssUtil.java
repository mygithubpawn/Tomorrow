package com.pawn.blog.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/***
 * description:  *对象存储工具类->oss
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2021/1/13 21:49
 */
@Component
public class OssUtil {

    /**
     * 域名
     */
    private final static String OSS_END_POINT = "";

    /**
     * #阿里云API的密钥Access Key ID
     * 账号
     */
    private final static String OSS_ACCESS_KEY_ID = "";

    /**
     * 阿里云API的密钥Access Key Secret
     * 密匙
     */
    private final static String OSS_ACCESS_KEY_SECRET = "";

    /**
     * 存储空间
     */
    private final static String OSS_BUCKET_NAME = "";

    /**
     * URL有效期
     */
    private final static Date OSS_URL_EXPIRATION = DateUtils.addDays(new Date(), 365 * 10);


    private volatile static OSSClient instance = null;

    private OssUtil() {
    }

    /**
     * Oss 实例化
     *
     * @return
     */
    private static OSSClient getOssClient() {
        if (instance == null) {
            synchronized (OssUtil.class) {
                if (instance == null) {
                    instance = new OSSClient(OSS_END_POINT, OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET);
                }
            }
        }
        return instance;
    }


    /**
     * 文件路径的枚举
     */
    public enum FileDirType {

        //oss文件名
        ARTICLE_IMAGES("images"),
        ARTICLE_BOK("pawn-bok"),
        MENU("menu");

        private String dir;

        FileDirType(String dir) {
            this.dir = dir;
        }

        @JsonValue
        public String getDir() {
            return dir;
        }
    }


    /**
     * 当Bucket 不存在时候创建Bucket
     */
    private static void createBuchet() {
        try {
            if (!OssUtil.getOssClient().doesBucketExist(OSS_BUCKET_NAME)) {
                OssUtil.getOssClient().createBucket(OSS_BUCKET_NAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClientException("创建Bucket失败,请核对Bucket名称(规则：只能包含小写字母、数字和短横线，必须以小写字母和数字开头和结尾，长度在3-63之间)");
        }
    }


    /**
     * 文件上传的文件后缀
     *
     * @param FilenameExtension
     * @return
     */
    private String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase("jpeg") ||
                FilenameExtension.equalsIgnoreCase("jpg") ||
                FilenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        return "multipart/form-data";
    }


    /**
     * 上传OSS服务器 如果同名文件会覆盖服务器上的
     *
     * @param file
     * @param fileDirType
     * @return
     */
    private static String uploadFile(MultipartFile file, FileDirType fileDirType) {
        String fileName = String.format("%s.%s", UUID.randomUUID().toString(), FilenameUtils.getExtension(file.getOriginalFilename()));
        try (InputStream inputStream = file.getInputStream()) {

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(FilenameUtils.getExtension("." + file.getOriginalFilename()));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            PutObjectResult putObjectResult = OssUtil.getOssClient().putObject(OSS_BUCKET_NAME, fileDirType.getDir() + "/" + fileName, inputStream, objectMetadata);
            System.out.println(putObjectResult);
            return fileDirType.getDir() + "/" + fileName;
        } catch (OSSException oe) {
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:    " + oe.getErrorCode());
            System.out.println("Request ID:    " + oe.getRequestId());
            System.out.println("Host ID:       " + oe.getHostId());
            return null;
        } catch (ClientException ce) {
            System.out.println("Error Message: " + ce.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Error Message: " + e.getMessage());
            return null;
        }
    }


    /**
     * 获取文件路径
     *
     * @param fileUrl
     * @param fileDirType
     * @return
     */
    private static String getFileUrl(String fileUrl, FileDirType fileDirType) {
        if (StringUtils.isEmpty(fileUrl)) {
            throw new RuntimeException("文件地址为空!");
        }
        String[] split = fileUrl.split("/");

        URL url = OssUtil.getOssClient().generatePresignedUrl(OSS_BUCKET_NAME, fileDirType.getDir() + "/" + split[split.length - 1], OSS_URL_EXPIRATION);
        if (url == null) {
            throw new RuntimeException("获取OSS文件URL失败!");
        }
        return url.toString();
    }


    /**
     * 文件上传 去掉URL中的?后的时间戳
     *
     * @param file
     * @param fileDirType
     * @return
     */
    public String upload(MultipartFile file, FileDirType fileDirType) {
        OssUtil.createBuchet();
        String fileName = OssUtil.uploadFile(file, fileDirType);
        String fileOssUrl = OssUtil.getFileUrl(fileName, fileDirType);
        int firstChar = fileOssUrl.indexOf("?");
        if (firstChar > 0) {
            fileOssUrl = fileOssUrl.substring(0, firstChar);
        }
        return fileOssUrl;
    }


    /**
     * 获取路径地址
     *
     * @param fileName
     * @return
     */
    public static String getPathUrl(String fileName) {
        return fileName.substring(fileName.indexOf(OSS_END_POINT) + OSS_END_POINT.length() + 1);
    }


    /**
     * 文件删除
     *
     * @param keys
     */
    public static void delete(List<String> keys) {
        List<String> newKeys = keys.stream().map((item) -> {
            return OssUtil.getPathUrl(item);
        }).collect(Collectors.toList());
        try {
            DeleteObjectsResult deleteObjectsResult = OssUtil.getOssClient().deleteObjects(new DeleteObjectsRequest(OSS_BUCKET_NAME).withKeys(newKeys));
            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
            System.out.println(deletedObjects);
        } catch (OSSException oe) {
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:    " + oe.getErrorCode());
            System.out.println("Request ID:    " + oe.getRequestId());
            System.out.println("Host ID:       " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Error Message: " + ce.getMessage());
        }

    }

}
