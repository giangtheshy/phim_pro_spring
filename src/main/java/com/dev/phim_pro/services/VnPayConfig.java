package com.dev.phim_pro.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class VnPayConfig {

    @Value("${vnp_PayUrl}")
    private String vnp_PayUrl;
    @Value("${client.url}")
    private String clientUrl;
    @Value("${vnp_Returnurl}")
    private String vnp_Returnurl;
    @Value("${vnp_TmnCode}")
    private String vnp_TmnCode;
    @Value("${vnp_HashSecret}")
    private String vnp_HashSecret;

    public String md5(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));

            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    public String Sha256(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));

            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }

    public String hashAllFields(Map fields) throws UnsupportedEncodingException {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder sb = new StringBuilder();
        sb.append(vnp_HashSecret);
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(URLDecoder.decode(fieldValue, "UTF-8"));

            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return Sha256(sb.toString());
    }
    public String getIpAddress(HttpServletRequest req){
        String ipAddress;
        try {
            ipAddress= req.getHeader("X-FORWARDED-FOR");
            if(ipAddress==null){
                ipAddress=req.getRemoteAddr();
            }

        }catch (Exception e){
            ipAddress="Invalid IP:"+e.getMessage();
        }
        return ipAddress;
    }
    public String getRandomNumber(int len){
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
