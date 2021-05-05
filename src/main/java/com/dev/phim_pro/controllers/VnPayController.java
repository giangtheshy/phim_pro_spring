package com.dev.phim_pro.controllers;

import com.dev.phim_pro.services.AuthService;
import com.dev.phim_pro.services.VnPayConfig;
import com.dev.phim_pro.dto.VnPayResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
public class VnPayController {

    private final VnPayConfig vnPayConfig;
    private final AuthService authService;


    @PostMapping("/create")
    public ResponseEntity<VnPayResponse> createPayment(HttpServletRequest req) throws ServletException,
            IOException {
        String vnp_Version = "2.0.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo =
                "Thanh toan nang cap tai khoan premium phim pro"+vnPayConfig.getRandomNumber(5);
        String vnp_TxnRef =
                authService.getCurrentUser().getId().toString()+"_"+vnPayConfig.getRandomNumber(5);
        String vnp_IpAddr = vnPayConfig.getIpAddress(req);
        String vnp_TmnCode = vnPayConfig.getVnp_TmnCode();
        Integer amount = 50000 * 100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl",vnPayConfig.getClientUrl()+"/payment/result");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(dt);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {

                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(fieldValue);

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnPayConfig.Sha256(vnPayConfig.getVnp_HashSecret() + hashData.toString());
        queryUrl += "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        VnPayResponse vnPayResponse = new VnPayResponse();
        vnPayResponse.setCode("00");
        vnPayResponse.setData(paymentUrl);
        return ResponseEntity.status(HttpStatus.OK).body(vnPayResponse);
    }

    @GetMapping("/return_vnp")
    public ResponseEntity<VnPayResponse> returnVnPay(HttpServletRequest request) throws UnsupportedEncodingException {
        Map fields = new HashMap();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String signValue = vnPayConfig.hashAllFields(fields);

        VnPayResponse vnPayResponse = new VnPayResponse();
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                Long id =Long.parseLong(request.getParameter("vnp_TxnRef").split("_")[0]) ;
                authService.updateTypeUser(id);
                vnPayResponse.setCode("00");
                vnPayResponse.setData("success");
            } else {
                vnPayResponse.setCode("97");
                vnPayResponse.setData("failure");
            }
            return ResponseEntity.status(HttpStatus.OK).body(vnPayResponse);

        } else {
            vnPayResponse.setCode("97");
            vnPayResponse.setData("failure");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(vnPayResponse);
        }

    }
    @GetMapping("/vnpay_ipn")
    public ResponseEntity<VnPayResponse> vnPayIpn(HttpServletRequest request) throws UnsupportedEncodingException {
        Map fields = new HashMap();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String signValue = vnPayConfig.hashAllFields(fields);

        VnPayResponse vnPayResponse = new VnPayResponse();
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                Long id =Long.parseLong(request.getParameter("vnp_TxnRef").split("_")[0]) ;
                authService.updateTypeUser(id);
                vnPayResponse.setCode("00");
                vnPayResponse.setData("success");
            } else {
                vnPayResponse.setCode("97");
                vnPayResponse.setData("failure");
            }
            return ResponseEntity.status(HttpStatus.OK).body(vnPayResponse);

        } else {
            vnPayResponse.setCode("97");
            vnPayResponse.setData("failure");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(vnPayResponse);
        }

    }
}
