package com.onlinestore.buy.controllers;

import com.onlinestore.buy.dtos.ImageDto;
import com.onlinestore.buy.entities.Image;
import com.onlinestore.buy.responses.APIResponse;
import com.onlinestore.buy.services.IImageService;
import com.onlinestore.buy.services.LLMService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("${api.version}/images")
@RequiredArgsConstructor
public class ImageController {
    private final IImageService imageService;
    private final LLMService llmService;

    @PostMapping("/upload")
    public ResponseEntity<APIResponse> uploadImage(@RequestParam("files") List<MultipartFile> files, @RequestParam("productId") Long productId){
        List<ImageDto> imageDtoList = imageService.saveImages(productId, files);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse( "Image uploaded successfully", imageDtoList));
    }

    @GetMapping("/image/{id}/download")
    public ResponseEntity<Resource> downloadImage(@PathVariable("id") Long id) throws SQLException {
        Image image = imageService.getImageById(id);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Disposition", "attachment; filename=\"" + image.getFileName()+"\"")
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .body(resource);
    }

    @PutMapping("/image/{id}/update")
    public ResponseEntity<APIResponse> updateImage(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file, @RequestParam("productId") Long productId){
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Image updated successfully", imageService.updateImage(id, productId, file)));
    }

    @DeleteMapping("/image/{id}/delete")
    public ResponseEntity<APIResponse> deleteImage(@PathVariable("id") Long id){
        imageService.deleteImageById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Image deleted successfully", null));
    }

    @GetMapping("/describe-image")
    public ResponseEntity<APIResponse> describeImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IOException {
        String description = llmService.describeImage(image);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("Image description", description));
    }
}
