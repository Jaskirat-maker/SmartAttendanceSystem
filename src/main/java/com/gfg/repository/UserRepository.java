package com.gfg.repository;

import com.gfg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // No need to add save() — it's inherited from JpaRepository
}
