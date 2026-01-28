package com.onlinestore.buy.services;

import com.onlinestore.buy.dtos.ImageDto;
import com.onlinestore.buy.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    ImageDto updateImage(Long imageId, Long productId, MultipartFile file);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
}
