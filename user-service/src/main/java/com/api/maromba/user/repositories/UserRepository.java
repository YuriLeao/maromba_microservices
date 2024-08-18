package com.api.maromba.user.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.maromba.user.models.AuthorizationModel;
import com.api.maromba.user.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

	boolean existsByEmail(String email);

	Optional<UserModel> findByEmailAndPassword(String email, String password);

	@Query("SELECT u FROM UserModel u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%',:name,'%'))")
	Page<UserModel> findByNameLike(Pageable pageable, @Param("name") String name);

	@Query("SELECT u FROM UserModel u WHERE u.companyId = :companyId AND u.authorization IN :authorizationsId")
	Page<UserModel> findByCompanyIdAndInAuthorization(Pageable pageable, @Param("companyId") UUID companyId,
			@Param("authorizationsId") List<AuthorizationModel> authorizationsId);
}
