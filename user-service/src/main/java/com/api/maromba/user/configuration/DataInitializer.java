package com.api.maromba.user.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.maromba.user.models.AuthorizationModel;
import com.api.maromba.user.models.GenderModel;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
    	createGenders();
    	
    	createAuthorizations();
    }

	private void createGenders() {
    	if (entityManager.find(GenderModel.class, "M") == null) {
    		entityManager.persist(new GenderModel("M", "Monstro"));
    	}
    	
    	if (entityManager.find(GenderModel.class, "F") == null) {
    		entityManager.persist(new GenderModel("F", "Monstra"));
    	}
    	
    	if (entityManager.find(GenderModel.class, "O") == null) {
    		entityManager.persist(new GenderModel("O", "Saiu da jaula"));
    	}
	}

	private void createAuthorizations() {
    	if (entityManager.find(AuthorizationModel.class, "A") == null) {
    		entityManager.persist(new AuthorizationModel("A", "Admin"));
    	}
    	
    	if (entityManager.find(AuthorizationModel.class, "E") == null) {
    		entityManager.persist(new AuthorizationModel("E", "Empresa"));
    	}
    	
    	if (entityManager.find(AuthorizationModel.class, "P") == null) {
    		entityManager.persist(new AuthorizationModel("P", "Professor"));
    	}
    	
    	if (entityManager.find(AuthorizationModel.class, "AL") == null) {
    		entityManager.persist(new AuthorizationModel("AL", "Aluno"));
    	}
	}
}
