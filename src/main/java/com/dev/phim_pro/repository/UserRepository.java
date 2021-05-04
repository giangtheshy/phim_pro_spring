package com.dev.phim_pro.repository;

import com.dev.phim_pro.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String userName);

    @Modifying
    @Query(value = "UPDATE user SET type = 'premium' WHERE (id = ?1);",nativeQuery = true)
    void updateTypeUser(Long id);
}
