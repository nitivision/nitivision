package com.niti.vision.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "job")
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String location;

	private String category; // e.g. Full-time, Part-time, Contract

	private LocalDate postedDate = LocalDate.now();

	@Column(length = 500)
	private String summary; // short preview

	@Lob
	private String description; // full description

	@ElementCollection
	@CollectionTable(name = "job_responsibilities", joinColumns = @JoinColumn(name = "job_id"))
	@Column(name = "responsibility")
	private List<String> responsibilities;

	@ElementCollection
	@CollectionTable(name = "job_requirements", joinColumns = @JoinColumn(name = "job_id"))
	@Column(name = "requirement")
	private List<String> requirements;

	@PrePersist
	@PreUpdate
	public void autoGenerateSummary() {
		if ((summary == null || summary.trim().isEmpty()) && description != null) {
			this.summary = description.length() > 200 ? description.substring(0, 200) + "..." : description;
		}
	}
	@CreationTimestamp
	private LocalDateTime createdAt;
	// ===== Getters and Setters =====
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDate getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDate postedDate) {
		this.postedDate = postedDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(List<String> responsibilities) {
		this.responsibilities = responsibilities;
	}

	public List<String> getRequirements() {
		return requirements;
	}

	public void setRequirements(List<String> requirements) {
		this.requirements = requirements;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
