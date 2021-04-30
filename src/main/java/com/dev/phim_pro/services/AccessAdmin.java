package com.dev.phim_pro.services;

import com.dev.phim_pro.exceptions.SpringPhimException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AccessAdmin {
    private final AuthService authService;

    public void isAccess(){
        if(!authService.getCurrentUser().getRole()){
            throw new SpringPhimException("Action had been denied by website!");
        }
    }
}
