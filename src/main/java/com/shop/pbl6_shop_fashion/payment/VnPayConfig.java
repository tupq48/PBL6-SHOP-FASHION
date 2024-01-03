
package com.shop.pbl6_shop_fashion.payment;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author CTT VNPAY
 */
public class VnPayConfig {

    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "https://pbl6shopfashion-production.up.railway.app/api/payments/vnpay/callback";
//        public static String vnp_ReturnUrl = "http://localhost:8080/api/payments/vnpay/callback";
    public static String vnp_TmnCode = "ZQ1H9MRJ";
    public static String secretKey = "IWKDEVBNCECMZFLHYNMLVDOLPFZFMIEZ";
    public static String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
    public static String transactionStatusSuccessful = "00";

    //Util for VN_PAY
    public static String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return hmacSHA512(secretKey, sb.toString());
    }

    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public static String getRandomNumber() {
        final int randomLength = 4;
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(randomLength);
        for (int i = 0; i < randomLength; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        long timestamp = System.currentTimeMillis();
        return timestamp + sb.toString();
    }

    public static String getTransactionStatusMessage(String statusCode) {
        switch (statusCode) {
            case "00":
                return "Giao dịch thành công";
            case "01":
                return "Giao dịch chưa hoàn tất";
            case "02":
                return "Giao dịch bị lỗi";
            case "04":
                return "Giao dịch đảo (Khách hàng đã bị trừ tiền tại Ngân hàng nhưng GD chưa thành công ở VNPAY)";
            case "05":
                return "VNPAY đang xử lý giao dịch này (GD hoàn tiền)";
            case "06":
                return "VNPAY đã gửi yêu cầu hoàn tiền sang Ngân hàng (GD hoàn tiền)";
            case "07":
                return "Giao dịch bị nghi ngờ gian lận";
            case "09":
                return "GD Hoàn trả bị từ chối";
            case "10":
                return "Đã giao hàng";
            case "11":
                return "Giao dịch bị hủy";
            case "20":
                return "Giao dịch đã được thanh quyết toán cho merchant";
            default:
                return "Mã trạng thái không hợp lệ";
        }
    }

    public static String getRefundStatusMessage(String statusCode) {
        switch (statusCode) {
            case "00":
                return "Giao dịch thành công";
            case "02":
                return "Merchant không hợp lệ (kiểm tra lại vnp_TmnCode)";
            case "03":
                return "Dữ liệu gửi sang không đúng định dạng";
            case "08":
                return "Hệ thống đang bảo trì";
            case "16":
                return "Không thực hiện được hoàn tiền trong thời gian này";
            case "91":
                return "Không tìm thấy giao dịch yêu cầu hoàn trả";
            case "93":
                return "Số tiền hoàn trả không hợp lệ. Số tiền hoàn trả phải nhỏ hơn hoặc bằng số tiền thanh toán.";
            case "94":
                return "Giao dịch đã được gửi yêu cầu hoàn tiền trước đó. Yêu cầu này VNPAY đang xử lý";
            case "95":
                return "Giao dịch này không thành công bên VNPAY. VNPAY từ chối xử lý yêu cầu.";
            case "97":
                return "Chữ ký không hợp lệ";
            default:
                return "Mã trạng thái không hợp lệ";
        }
    }

    public static String getPaymentMessage(String errorCode) {
        errorCode = errorCode.trim(); // Trim leading and trailing whitespaces
        switch (errorCode) {
            case "00":
                return "Giao dịch thành công";
            case "05":
                return "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
            case "06":
                return "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "07":
                return "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường). Đối với giao dịch này cần merchant xác nhận thông qua Tài liệu đặc tả kỹ thuật kết nối Merchant TMĐT - Cổng thanh toán VNPAY v2.1.0";
            // Add more cases as needed
            case "09":
                return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.";
            case "10":
                return "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần.";
            case "11":
                return "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "12":
                return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
            case "24":
                return "Giao dịch không thành công do: Khách hàng hủy giao dịch.";
            case "79":
                return "Giao dịch không thành công do: Quý khách nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "65":
                return "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
            case "75":
                return "Ngân hàng thanh toán đang bảo trì.";
            case "99":
                return "Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê).";
            default:
                return "Mã lỗi không hợp lệ";
        }
    }


}
