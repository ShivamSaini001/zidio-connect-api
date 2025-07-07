package com.zidio.connect.repository;

import com.zidio.connect.entities.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;



@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {

	Optional<UserAuthority> findByAuthority(String authority);
}
	