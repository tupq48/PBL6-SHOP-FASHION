package com.shop.pbl6_shop_fashion.payment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnPayService implements PaymentService {

    @Override
    public String getUrlPayment(long total, String orderInfo, String vnp_TxnRef) {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        if (vnp_TxnRef == null) {
            vnp_TxnRef = VnPayConfig.getRandomNumber();
        }
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;
        String orderType = "order-type";
        String locate = "vn";

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", locate);
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VnPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    @Override
    public String refundPayment(String vnp_TxnRef, long amountOrder, String vnp_PayDate) throws IOException {
        //Command: refund
        String vnp_RequestId = VnPayConfig.getRandomNumber();
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TransactionType = "02"; //Refund toan bo
        long amount = amountOrder * 100;
        String vnp_Amount = String.valueOf(amount);
        String vnp_OrderInfo = "Hoan tien GD OrderId: " + vnp_TxnRef;
        String vnp_TransactionNo = ""; //Assuming value of the parameter "vnp_TransactionNo" does not exist on your system.
        String vnp_TransactionDate = vnp_PayDate;
        String vnp_CreateBy = "user";

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());


        JsonObject vnp_Params = new JsonObject();

        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", vnp_Amount);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);

        if (vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty()) {
            vnp_Params.addProperty("vnp_TransactionNo", "{get value of vnp_TransactionNo}");
        }

        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo, vnp_TransactionDate,
                vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hash_Data);

        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        URL url = new URL(VnPayConfig.vnp_ApiUrl);
        String requestBody = new Gson().toJson(vnp_Params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(url.toURI(), requestEntity, String.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        var responseBody = responseEntity.getBody();
        if (responseBody != null) {
            System.out.println("Response Body: " + responseBody);
        }
        return concatenateResponseAndTransactionStatus(String.valueOf(responseBody));

    }

    public String concatenateResponseAndTransactionStatus(String jsonResponse) {
        String responseCode = getVnpMessage(jsonResponse, "vnp_ResponseCode");
        String transactionStatus = getVnpMessage(jsonResponse, "vnp_TransactionStatus");

        if (responseCode != null && transactionStatus != null) {
            return VnPayConfig.getTransactionStatusMessage(transactionStatus) + ", " + VnPayConfig.getRefundStatusMessage(responseCode);
        } else {
            return null; // Handle the case when either value is not found
        }
    }

    public String getVnpMessage(String jsonResponse, String key) {
        String keyString = "\"" + key + "\":\"";
        int startIndex = jsonResponse.indexOf(keyString);
        if (startIndex == -1) {
            return null;
        }
        startIndex += keyString.length();
        int endIndex = jsonResponse.indexOf("\"", startIndex);

        return jsonResponse.substring(startIndex, endIndex);
    }

    //    @Override
//    public String getPaymentCallBack(HttpServletRequest request) {
//        Map<String, String> fields = new HashMap<>();
//        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
//            String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
//            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                fields.put(fieldName, fieldValue);
//            }
//        }
//
//        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
//        fields.remove("vnp_SecureHashType");
//        fields.remove("vnp_SecureHash");
//        String signValue = VnPayConfig.hashAllFields(fields);
//        // Valid signature
//        if (signValue.equals(vnp_SecureHash)) {
//            String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
//            if (VnPayConfig.transactionStatusSuccessful.equals(vnp_TransactionStatus)) {
//                String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
//                String vnp_TxnRef = request.getParameter("vnp_TxnRef");
//                int idValue = 0;
//                if (vnp_OrderInfo.contains("OrderId:")) {
//                    idValue = Integer.parseInt(vnp_OrderInfo.split("OrderId:")[1].trim());
//                }
//                orderService.updateWithVnPayCallback(idValue, vnp_TxnRef);
//                return "1";
//            }
//            return "0";
//        } else {
//            return "-1";
//        }
//    }


}