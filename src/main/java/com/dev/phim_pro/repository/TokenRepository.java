package com.dev.phim_pro.repository;

import com.dev.phim_pro.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByToken(String token);

    void deleteByToken(String token);
}
