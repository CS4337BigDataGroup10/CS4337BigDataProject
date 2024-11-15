package com.example.UserManagementService.repository;


import com.example.UserManagementService.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepository extends JpaRepository<UserEntity, String> {
    // No additional methods needed for now
}