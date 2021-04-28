package com.dev.phim_pro.services;

import com.dev.phim_pro.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CardService {
    private final CardRepository cardRepository;
}
