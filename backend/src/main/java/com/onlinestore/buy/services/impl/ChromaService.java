package com.onlinestore.buy.services.impl;

import com.onlinestore.buy.requests.EmbeddingsDeleteRequest;
import com.onlinestore.buy.services.IChromaService;
import com.onlinestore.buy.services.LLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingsRequest;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;
import org.springframework.ai.chroma.vectorstore.ChromaApi.DeleteEmbeddingsRequest;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.ai.chroma.vectorstore.ChromaApi.QueryRequest.Include.all;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChromaService implements IChromaService {
    private final ChromaApi chromaApi;
    private final ChromaVectorStore chromaVectorStore;
    private final LLMService llmService;

    @Value("${spring.ai.vectorstore.chroma.tenant-name}")
    private String tenantName;
    @Value("${spring.ai.vectorstore.chroma.database-name}")
    private String databaseName;

    @Override
    public void deleteCollection(String collectionName) {
        try {
            chromaApi.deleteCollection(tenantName, databaseName, collectionName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete collection: "+collectionName);
        }
    }

    @Override
    public List<Collection> getCollections() {
        try {
            return chromaApi.listCollections(tenantName, databaseName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get collections");
        }
    }

    @Override
    public GetEmbeddingResponse getEmbeddings(String collectionId) {
        try {
            GetEmbeddingsRequest request = new GetEmbeddingsRequest(null, null, 4, 0, all);
            return chromaApi.getEmbeddings(tenantName, databaseName, collectionId, request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get embeddings from: "+collectionId);
        }
    }

    public String saveEmbeddings(MultipartFile image, Long productId, Long imageId) throws IOException {
        String imageDescription = llmService.describeImage(image);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("productId", productId);
        metadata.put("imageId", imageId);
        metadata.put("documentId", UUID.randomUUID().toString());
        Document document = Document.builder().id(imageId.toString())
                .text(imageDescription)
                .metadata(metadata)
                .build();
        try {
            chromaVectorStore.doAdd(List.of(document));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return "Image embeddings saved successfully";
    }

    @Override
    public void deleteEmbeddingsByCollectionId(EmbeddingsDeleteRequest request) {
        String collectionId = chromaApi.getCollection(tenantName, databaseName, request.getCollectionName()).id();
        List<String> idsToDelete = findIdsToDelete(collectionId, request.getImageId());
        if(idsToDelete.isEmpty()) {
            log.info("No embeddings found for image: {}", request.getImageId());
            return;
        }
        performDelete(collectionId, idsToDelete, request.getImageId());
    }

    public List<Long> searchImageSimilarity(MultipartFile image) throws IOException{
        String imageDescription = llmService.describeImage(image);
        SearchRequest request = SearchRequest.builder()
                .query(imageDescription)
                .topK(10)
                .similarityThreshold(0.85f)
                .build();
        List<Document> documents = chromaVectorStore.doSimilaritySearch(request);
        documents.forEach(document -> {
            Object distanceObject = document.getMetadata().get("distance");
            Double distance = null;
            if(distanceObject != null) distance = Double.parseDouble(distanceObject.toString());
            log.info("Search image similarity score: {}, ProductId: {}, Distance: {}", document.getScore(), document.getMetadata().get("productId"), distance);
        });
        return documents.stream().map(document -> document.getMetadata().get("productId"))
                .filter(Objects::nonNull).map(Objects::toString).map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private GetEmbeddingResponse fetchEmbeddingsByImageId(String collectionId, String imageId) {
        try {
            GetEmbeddingsRequest request = new GetEmbeddingsRequest(List.of(imageId), null, 100, 0, all);
            return chromaApi.getEmbeddings(tenantName, databaseName, collectionId, request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get embeddings from: "+collectionId);
        }
    }

    private List<String> findIdsToDelete(String collectionId, String imageId) {
        GetEmbeddingResponse embeddingResponse = fetchEmbeddingsByImageId(collectionId, imageId);
        List<String> idsToDelete = new ArrayList<>();
        if(embeddingResponse != null && !embeddingResponse.ids().isEmpty()) {
            idsToDelete.addAll(embeddingResponse.ids());
            log.info("Found embeddings by id: {}", idsToDelete);
        }
        return idsToDelete;
    }

    private void performDelete(String collectionId, List<String> idsToDelete, String imageId) {
        try {
            DeleteEmbeddingsRequest request = new DeleteEmbeddingsRequest(idsToDelete, null);
            log.info("Sending delete request: {}", request);
            int response = chromaApi.deleteEmbeddings(tenantName, databaseName, collectionId, request);
            log.info("Chroma delete response: {}", response);
        } catch (Exception e) {
            log.error("Failed to delete embeddings for imageId: {}, idsToDelete: {}", imageId, idsToDelete, e);
            throw new RuntimeException("Failed to delete embeddings from: "+collectionId+" for image id: "+imageId);
        }
    }
}
