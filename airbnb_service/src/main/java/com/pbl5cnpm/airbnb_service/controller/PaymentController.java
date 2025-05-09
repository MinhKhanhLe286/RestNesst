package com.pbl5cnpm.airbnb_service.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pbl5cnpm.airbnb_service.assistants.HMACutill;
import com.pbl5cnpm.airbnb_service.dto.Request.CreatePaymentRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.entity.PaymentEntity;
import com.pbl5cnpm.airbnb_service.repository.CreateInfoPaymentRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;
import com.pbl5cnpm.airbnb_service.service.CreatepaymentService;
import com.pbl5cnpm.airbnb_service.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final CreatepaymentService CreatepaymentService;
    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnp_Returnurl;

    @PostMapping("/create-payment")
    public ApiResponse<String> createPayment(
            @RequestParam("paymethod") String method,
            @RequestBody CreatePaymentRequest body,
            HttpServletRequest request) throws UnsupportedEncodingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (method.equals("VnPay")) {
            String ipClient = getClientIp(request);
            String toUrl = createVNPay(ipClient, body, username);
            return ApiResponse.<String>builder()
                    .code(200)
                    .message("Create payment successfully")
                    .result(toUrl)
                    .build();
        }

        return null;
    }

    private String createVNPay(String ipClient, CreatePaymentRequest body, String username)
            throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "170000";
        String vnp_TxnRef = this.CreatepaymentService.createPaymentInfo(body, username);
        String vnp_IpAddr = ipClient;

        Long amount = body.getAmount();
        String vnp_TxnAmount = String.valueOf(amount * 100);
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_TxnAmount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Sắp xếp tham số
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String value = vnp_Params.get(fieldName);
            if (value != null && !value.isEmpty()) {
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()))
                        .append('&');
                query.append(fieldName).append('=')
                        .append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()))
                        .append('&');
            }
        }

        hashData.deleteCharAt(hashData.length() - 1);
        query.deleteCharAt(query.length() - 1);

        String secureHash = HMACutill.hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        String paymentUrl = vnp_PayUrl + "?" + query.toString();
        return paymentUrl;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @GetMapping("/my")
    public List<PaymentEntity> getMethodName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return this.paymentService.getmypayment(username);
    }

}