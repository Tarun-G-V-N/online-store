package com.onlinestore.buy.services;

import com.onlinestore.buy.requests.EmbeddingsDeleteRequest;
import org.springframework.ai.chroma.vectorstore.ChromaApi.Collection;
import org.springframework.ai.chroma.vectorstore.ChromaApi.GetEmbeddingResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IChromaService {
    void deleteCollection(String collectionName);
    List<Collection> getCollections();
    GetEmbeddingResponse getEmbeddings(String collectionName);

    void deleteEmbeddingsByCollectionId(EmbeddingsDeleteRequest request);
}
