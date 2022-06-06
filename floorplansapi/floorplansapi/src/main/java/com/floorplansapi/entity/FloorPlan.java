package com.floorplansapi.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/*
 * FloorPlan class to generate table in database with 
 * id as primary key auto-generated, name, originalImage, thumbImage, largeImage column
 * This store all the information related to floorPlan and has foreign key map to project_id
*/
@Entity
@Table(name = "floorPlan")
@EntityListeners(AuditingEntityListener.class)
public class FloorPlan implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Long id;

	@Column(name = "floorPlanName", nullable = false)
	private String name;

	@Lob
	private byte[] originalImage;
	@Lob
	private byte[] thumImage;
	@Lob
	private byte[] largeImage;

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

	public byte[] getOriginalImage() {
		return originalImage;
	}

	public void setOriginalImage(byte[] originalImage) {
		this.originalImage = originalImage;
	}

	public byte[] getThum() {
		return thumImage;
	}

	public void setThum(byte[] thumImage) {
		this.thumImage = thumImage;
	}

	public byte[] getLarge() {
		return largeImage;
	}

	public void setLarge(byte[] largeImage) {
		this.largeImage = largeImage;
	}

}
