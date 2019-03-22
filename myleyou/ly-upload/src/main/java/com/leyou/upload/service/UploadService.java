/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: UploadService
 * Author:   gyr
 * Date:     2019/3/5 19:36
 * Description:
 */
package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties prop;

    //private static final List<String> ALLOW_TYPES = Arrays.asList("image/jpeg", "image/png", "image/bmp");

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    public String uploadImage(MultipartFile file) {
        try {
            //校验文件类型
            String contentType = file.getContentType();
            if (!prop.getAllowTypes().contains(contentType)) {
                throw new LyException(ExceptionEnums.UPLOAD_FILE_TYPE_ERROR);
            }

            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnums.UPLOAD_FILE_TYPE_ERROR);
            }

            // 上传到FastDFS
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), "."); // 在.的后面截取
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);

            //返回路径
            return prop.getBaseUrl() + storePath.getFullPath();
        } catch (IOException e) {
            //上传失败
            log.error("上传文件失败", e);
            throw new LyException(ExceptionEnums.UPLOAD_ERROR);
        }
    }
}