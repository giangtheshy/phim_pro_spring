package com.dev.phim_pro.repository;

import com.dev.phim_pro.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String userName);

    @Query(value = "SELECT * FROM user WHERE username=?1",nativeQuery = true)
    User findByUser(String userName);

    @Modifying
    @Query(value = "UPDATE user SET type = 'premium' WHERE (id = ?1);",nativeQuery = true)
    void updateTypeUser(Long id);

    @Query(value = "SELECT * FROM user WHERE username=?1 AND enable=true",nativeQuery = true)
    Optional<User> findByUserNameAndEnable(String username);
}
