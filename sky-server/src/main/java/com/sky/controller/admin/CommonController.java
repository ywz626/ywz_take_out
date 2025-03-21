package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author 于汶泽
 */
@RestController
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("admin/common/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file)  {
        String originalFilename = file.getOriginalFilename();
        String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
        String finalName = UUID.randomUUID().toString() + extention;
        String url = null;
        try {
            url = aliOssUtil.upload(file.getBytes(), finalName);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage());
            return Result.error(e.getMessage());
        }
        return Result.success(url);
    }
}
