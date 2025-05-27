package com.zidio.connect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zidio.connect.entities.Skill;


@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>{

	Optional<Skill> findByName(String name);
}
