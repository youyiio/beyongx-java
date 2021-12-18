package com.beyongx.system.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.beyongx.common.utils.FileUtils;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.utils.FileUploader;
import com.beyongx.system.entity.SysFile;
import com.beyongx.system.service.ISysFileService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 文件控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/file")
@Slf4j
public class FileController {
    
    @Value("${beyongx.file.save_path}")
    private String savePath;
    @Value("${beyongx.file.url_relative_path}")
    private String urlRelativePath;

    @Autowired
    private ISysFileService fileService;
    
    @RequiresPermissions("file:upload")
    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file") MultipartFile file, @RequestParam Map<String, Object> map) throws IOException {        

        String originFileName = file.getOriginalFilename();
        String name = FileUtils.getFileNameNoExt(originFileName);
        String ext = FileUtils.getFileExtensionFromName(originFileName).toLowerCase();
        if (StringUtils.isBlank(originFileName)) {
            return Result.error(Result.Code.E_PARAM_VALIDATE_ERROR, "文件名不能为空!");
        }

        Set<String> validExts = new HashSet<>();
        validExts.addAll(Arrays.asList(FileUploader.FORMAT_DOCUMENT.split(" ")));
        if (!validExts.contains(ext)) {
            return Result.error(Result.Code.E_PARAM_VALIDATE_ERROR, "文件格式不正确!");
        }
        
        File saveFile = FileUploader.upload(file, savePath);
        if (saveFile == null) {
            log.warn("文件上传失败: {}", originFileName);
            return Result.error(Result.Code.ACTION_FAILED, "文件上传失败");
        }

        String filePath = saveFile.getCanonicalPath();
        String fileUrl = filePath.replace(File.separator, "/").replace(savePath.replace(File.separator, "/"), "");
        fileUrl = urlRelativePath + fileUrl;

        SysFile sysFile = new SysFile();
        sysFile.setFileUrl(fileUrl);
        sysFile.setFilePath(filePath);
        sysFile.setName(saveFile.getName());
        sysFile.setRealName(originFileName);
        sysFile.setSize(saveFile.length());
        sysFile.setExt(ext);
        sysFile.setRemark(name);
        sysFile.setCreateTime(new Date());

        fileService.save(sysFile);

        return Result.success(sysFile);
    }
}
