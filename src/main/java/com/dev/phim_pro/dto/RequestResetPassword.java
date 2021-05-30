package com.dev.phim_pro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResetPassword {
    private String password;
    private String confirmPassword;
}
