package com.onlinestore.buy.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmbeddingsDeleteRequest {
    private String collectionName;
    private String imageId;
}
