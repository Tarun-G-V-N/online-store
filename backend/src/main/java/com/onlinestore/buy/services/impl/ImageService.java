package com.onlinestore.buy.services.impl;

import com.onlinestore.buy.dtos.ImageDto;
import com.onlinestore.buy.entities.Image;
import com.onlinestore.buy.entities.Product;
import com.onlinestore.buy.repositories.ImageRepository;
import com.onlinestore.buy.requests.EmbeddingsDeleteRequest;
import com.onlinestore.buy.services.IImageService;
import com.onlinestore.buy.services.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;
    private final ChromaService chromaService;

    @Value("${spring.ai.vectorstore.chroma.collection-name}")
    private String collectionName;

    @Autowired
    public ImageService(ImageRepository imageRepository, IProductService productService, ChromaService chromaService) {
        this.imageRepository = imageRepository;
        this.productService = productService;
        this.chromaService = chromaService;
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new EntityNotFoundException("Image not found");
        });
        EmbeddingsDeleteRequest deleteRequest = EmbeddingsDeleteRequest.builder()
                .collectionName(collectionName)
                .imageId(id.toString())
                .build();
        chromaService.deleteEmbeddingsByCollectionId(deleteRequest);
    }

    @Override
    public ImageDto updateImage(Long imageId, Long productId, MultipartFile file) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Image not found"));
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            Image savedImage = imageRepository.save(image);

            EmbeddingsDeleteRequest deleteRequest = EmbeddingsDeleteRequest.builder()
                    .collectionName(collectionName)
                    .imageId(imageId.toString())
                    .build();
            chromaService.deleteEmbeddingsByCollectionId(deleteRequest);

            chromaService.saveEmbeddings(file, productId, savedImage.getId());

            ImageDto imageDto = new ImageDto();
            imageDto.setId(savedImage.getId());
            imageDto.setFileName(savedImage.getFileName());
            imageDto.setDownloadUrl(savedImage.getDownloadUrl());
            return imageDto;
        }
        catch (IOException | SQLException exception) {
            throw new RuntimeException("Failed to update image", exception);
        }
    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setProduct(product);
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));

                Image savedImage = imageRepository.save(image);
                String buildDownloadUrl = "api/v1/images/image/download";
                savedImage.setDownloadUrl(buildDownloadUrl + "/" + savedImage.getId());
                imageRepository.save(savedImage);

                String saveEmbeddingsResult = chromaService.saveEmbeddings(file, productId, savedImage.getId());
                log.info(saveEmbeddingsResult);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImages.add(imageDto);
            }
            catch (IOException | SQLException exception) {
                throw new RuntimeException("Failed to update image", exception);
            }
        }
        return savedImages;
    }
}
