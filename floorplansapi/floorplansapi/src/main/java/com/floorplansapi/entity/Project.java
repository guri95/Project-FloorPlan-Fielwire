package com.floorplansapi.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/*
 * Project class map to projecct_info table in database to store
 * id as primary key auto-generated, name as projectName and floorPlan mapped
 * to floorplan_info table as foreign key.
*/
@Entity
@Table(name = "project_info")
@EntityListeners(AuditingEntityListener.class)
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "project_id")
	private Long id;

	@Column(name = "projectName", nullable = false)
	private String name;

	@OneToMany(targetEntity = FloorPlan.class, cascade = CascadeType.ALL)

	@JoinColumn(name = "project_fk", referencedColumnName = "project_id")

	private List<FloorPlan> floorPlan;

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdTime;

	@LastModifiedDate
	@Column(updatable = true, nullable = false)
	private LocalDateTime lastModifiedDate;

	public List<FloorPlan> getFloorPlan() {
		return floorPlan;
	}

	public void setFloorPlan(List<FloorPlan> floorPlan) {
		this.floorPlan = floorPlan;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

}