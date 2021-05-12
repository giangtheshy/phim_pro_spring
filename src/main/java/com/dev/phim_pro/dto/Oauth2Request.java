package com.dev.phim_pro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2Request {
    private String name;
    private String avatar;
    private String email;
}
