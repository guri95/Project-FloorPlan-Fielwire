package com.floorplansapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.floorplansapi.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
