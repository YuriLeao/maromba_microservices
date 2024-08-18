package com.api.maromba.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.user.models.GenderModel;

public interface GenderRepository extends JpaRepository<GenderModel, String> {
}
