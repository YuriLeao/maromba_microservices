package com.api.maromba.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.maromba.user.models.AuthorizationModel;

public interface AuthorizationRepository extends JpaRepository<AuthorizationModel, String> {

}
