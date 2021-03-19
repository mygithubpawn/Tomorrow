package com.pawn.blog.service;

import com.pawn.blog.entity.Images;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.response.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface ImagesService extends IService<Images> {

    Result uploadImage(String userId,String original,MultipartFile file);

    Result getImages(String imagesId);

    Result listImages(String original,int page, int size);

    Result deleteImages(String imagesId);

    Result imageTotalPar();
}
