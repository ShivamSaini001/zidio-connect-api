package com.zidio.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zidio.connect.entities.AdminProfile;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, Long>{

}
