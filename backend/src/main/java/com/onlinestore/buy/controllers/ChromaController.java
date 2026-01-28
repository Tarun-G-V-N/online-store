package com.onlinestore.buy.controllers;

import com.onlinestore.buy.responses.APIResponse;
import com.onlinestore.buy.services.IChromaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.version}/collections")
public class ChromaController {
    private final IChromaService chromaService;

    @GetMapping
    public ResponseEntity<APIResponse> getCollections() {
        return ResponseEntity.ok(new APIResponse("Collections fetched successfully", chromaService.getCollections()));
    }

    @GetMapping("/{collectionId}/embeddings")
    public ResponseEntity<APIResponse> getCollectionEmbeddings(@PathVariable("collectionId") String collectionId) {
        return ResponseEntity.ok(new APIResponse("Embeddings fetched successfully", chromaService.getEmbeddings(collectionId)));
    }

    @DeleteMapping("/{collectionName}/delete")
    public ResponseEntity<APIResponse> deleteCollection(@PathVariable("collectionName") String collectionName) {
        chromaService.deleteCollection(collectionName);
        return ResponseEntity.ok(new APIResponse("Collection deleted successfully", null));
    }
}
