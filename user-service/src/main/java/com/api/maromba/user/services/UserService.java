package com.api.maromba.user.services;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.maromba.user.models.UserModel;
import com.api.maromba.user.repositories.UserRepository;
import com.api.maromba.user.util.Encrypt;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Encrypt criptor;
	
	@Transactional
	public UserModel save(UserModel userModel) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		userModel.setPassword(criptor.encryptPassword(userModel.getEmail(), userModel.getPassword()));
		return userRepository.save(userModel);
	}
	
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public Optional<UserModel> findByEmailAndPassword(String email, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		password = criptor.encryptPassword(email, password);
		return userRepository.findByEmailAndPassword(email, password);
	}

	@Transactional
	public void delete(UserModel userModel) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		userModel.setPassword(criptor.encryptPassword(userModel.getEmail(), userModel.getPassword()));
		userRepository.deleteByEmailAndPassword(userModel.getEmail(), userModel.getPassword());
	}

	public Page<UserModel> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	
	public List<UserModel> findByNameLike(String name) {
		return userRepository.findByNameLike(name);
	}

}
