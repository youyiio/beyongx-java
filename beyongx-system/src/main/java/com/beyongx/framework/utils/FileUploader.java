package com.beyongx.framework.utils;

import java.io.File;
import java.util.Date;

import com.beyongx.common.utils.DateTimeUtils;
import com.beyongx.common.utils.FileUtils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传处理类
 */
@Slf4j
public class FileUploader {
    
    //图片的文件后缀格式
    public static String FORMAT_IMAGE = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg webp";
    public static String FORMAT_DOCUMENT = "txt doc pdf ppt pps xlsx xls docx";
    public static String FORMAT_MUSIC = "mp3 wav wma mpa ram ra aac aif m4a";
    public static String FORMAT_VIDEO = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";

    /**
     * 
     * @param file springmvc 上传的文件封装类
     * @param savePath 文件保存目录
     * @return
     */
    public static File upload(MultipartFile file, String savePath) {
        String originFileName = file.getOriginalFilename();

        String name = FileUtils.getFileNameNoExt(originFileName);
        String suffix = FileUtils.getFileExtensionFromName(originFileName);

        Date currentDate = new Date();
        String fileName = RandomStringUtils.random(36, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_+&#@!") + "." + suffix;
        String dateDirectory = DateTimeUtils.getFormatString(currentDate, "yyyyMMdd");
        String path = savePath + File.separator + dateDirectory + File.separator + fileName;

        File dest = null;
        try {            
            // getCanonicalFile 可解析正确各种路径
            dest = new File(path).getCanonicalFile();
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) {
                    log.warn("mkdir file dir is failed.");
                    throw new Exception("when file upload, mkdir file dir is failed.");
                }
            }

            // 文件写入
            file.transferTo(dest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return dest;
    }
}
