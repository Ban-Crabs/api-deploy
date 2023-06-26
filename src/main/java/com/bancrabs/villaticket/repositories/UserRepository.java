package com.bancrabs.villaticket.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsernameOrEmail(String username, String email);
    Page<User> findAllByOrderByUsernameAsc(Pageable pageable);
    Page<User> findAllByOrderByUsernameDesc(Pageable pageable);
    Page<User> findByEmailOrUsernameContainsAllIgnoringCase(String username, String email, Pageable pageable);
}
