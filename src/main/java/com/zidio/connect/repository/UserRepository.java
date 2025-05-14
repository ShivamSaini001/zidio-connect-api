package com.zidio.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zidio.connect.entities.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	List<User> findByEmail(String email);
}
