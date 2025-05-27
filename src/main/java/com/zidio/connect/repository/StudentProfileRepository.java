package com.zidio.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zidio.connect.entities.StudentProfile;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long>{

}
