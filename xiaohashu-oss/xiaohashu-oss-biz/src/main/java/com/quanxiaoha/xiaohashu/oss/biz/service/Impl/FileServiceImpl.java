package com.quanxiaoha.xiaohashu.oss.biz.service.Impl;

import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.oss.biz.service.FileService;
import com.quanxiaoha.xiaohashu.oss.biz.strategy.FileStrategy;
import jakarta.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FileStrategy fileStrategy;

    private static final String BUCKET_NAME = "xiaohashu";
    @Override
    public Response<?> uploadFile(MultipartFile file) {
        //上传文件到
        String url = fileStrategy.uploadFile(file,"xiaohashu");
        return Response.success(url);
    }
}
