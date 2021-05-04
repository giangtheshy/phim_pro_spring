package com.dev.phim_pro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String authenticationToken;
    private String name;
    private String avatar;
    private Boolean role;
    private String type;
    private List<String> favorites;
    private List<String> watched;
}
