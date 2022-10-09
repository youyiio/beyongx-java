package com.beyongx.framework.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface IOssService {
    /**
     * 判断存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return
     */
    boolean isBucketExist(String bucketName);

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    void createBucket(String bucketName);

    void uploadObject(MultipartFile file, String bucketName, String fileName);

    /**
     * 获取访问对象的外链地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiry     过期时间(天) 最大为7天 超过7天则默认最大值
     * @return viewUrl
     */
    String getObjectUrl(String bucketName, String objectName, Integer expiry);

    /**
     * 创建上传文件对象的外链
     *
     * @param bucketName 存储桶名称
     * @param objectName 欲上传文件对象的名称
     * @param expiry     过期时间(天) 最大为7天 超过7天则默认最大值
     * @return uploadUrl
     */
    String createUploadUrl(String bucketName, String objectName, Integer expiry);

    /**
     * 批量创建分片上传外链
     *
     * @param bucketName 存储桶名称
     * @param objectMD5  欲上传分片文件主文件的MD5
     * @param chunkCount 分片数量
     * @return uploadChunkUrls
     */
    List<String> createUploadChunkUrlList(String bucketName, String objectMD5, Integer chunkCount);

    /**
     * 创建指定序号的分片文件上传外链
     *
     * @param bucketName 存储桶名称
     * @param objectMD5  欲上传分片文件主文件的MD5
     * @param partNumber 分片序号
     * @return uploadChunkUrl
     */
    String createUploadChunkUrl(String bucketName, String objectMD5, Integer partNumber);

    /**
     * 获取对象文件名称列表
     *
     * @param bucketName 存储桶名称
     * @param prefix     对象名称前缀
     * @return objectNames
     */
    List<String> listObjectNames(String bucketName, String prefix);

    /**
     * 获取分片文件名称列表
     *
     * @param bucketName 存储桶名称
     * @param objectMd5  对象Md5
     * @return objectChunkNames
     */
    List<String> listChunkObjectNames(String bucketName, String objectMd5);

    /**
     * 获取分片名称地址HashMap key=分片序号 value=分片文件地址
     *
     * @param bucketName 存储桶名称
     * @param objectMd5  对象Md5
     * @return objectChunkNameMap
     */
    Map<Integer, String> mapChunkObjectNames(String bucketName, String objectMd5);

    /**
     * 合并分片文件成对象文件
     *
     * @param chunkBucKetName   分片文件所在存储桶名称
     * @param composeBucketName 合并后的对象文件存储的存储桶名称
     * @param chunkNames        分片文件名称集合
     * @param objectName        合并后的对象文件名称
     * @return true/false
     */
    boolean composeObject(String chunkBucKetName, String composeBucketName, List<String> chunkNames, String objectName);
}
