package com.onlinestore.buy.requests;

import lombok.Data;

@Data
public class PaymentRequest {
    private int amount;
    private String currency;
}
