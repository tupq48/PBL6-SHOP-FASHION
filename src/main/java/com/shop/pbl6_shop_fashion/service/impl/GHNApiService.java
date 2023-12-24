package com.shop.pbl6_shop_fashion.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.pbl6_shop_fashion.dto.order.GHNApiRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GHNApiService {

    @Value("${application.ghn.url-api}")
    private String apiUrl;

    @Value("${application.ghn.token}")
    private String token;
    @Value("${application.ghn.shop-id}")
    private String shopId;
    @Value("${application.ghn.service_id}")
    private int serviceId;
    @Value("${application.ghn.service_type_id}")
    private int serviceTypeId;

    public long getShippingFee(long toDistrictId, String toWardCode) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Token", token);
        headers.add("ShopId", shopId);

        GHNApiRequest ghnApiRequest = GHNApiRequest.builder()
                .weight(100)
                .width(10)
                .length(10)
                .height(10)
                .serviceId(serviceId)
                .serviceTypeId(serviceTypeId)
                .toDistrictId(toDistrictId)
                .toWardCode(toWardCode)
                .build();
        try {
            HttpEntity<?> entity = new HttpEntity<>(ghnApiRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            String responseBody = response.getBody();
            return parseTotal(responseBody);
        } catch (Exception e) {
            throw new IllegalArgumentException("There was an issue with the delivery address. Please check and provide a valid address for delivery");
        }
    }

    private long parseTotal(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            if (jsonNode.has("data") && jsonNode.get("data").has("total")) {
                return jsonNode.get("data").get("total").asInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
