package com.api.maromba.executedWorkout.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table(name = "tb_executed_workout")
@Data
public class ExecutedWorkoutModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;
    
    @Column(nullable = false, length = 100)
    @Size(min = 1, max = 100)
    private String name;
    
    @Column(length = 500)
    @Size(max = 500)
    private String description;
    
    @OneToMany(mappedBy = "executedWorkout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutedDivisionModel> divisions;

	@Column(nullable = false, columnDefinition = "UUID")
    private UUID userId;

	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;
	
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime finishAt;
	
    @Column(nullable = false)
    private boolean isActive;
    
}
