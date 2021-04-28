package com.dev.phim_pro.services;

import com.dev.phim_pro.repository.EpisodeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
}
