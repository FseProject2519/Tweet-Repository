package com.tweetapp.authorization.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tweetapp.authorization.entity.UserEntity;
import com.tweetapp.authorization.repository.UserRepository;

@Service
public class DetailsService implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.info("Start - loadUserByUsername");
		try {
			UserEntity userData = userRepository.findByUserId(username).orElse(null);
			if (userData != null) {
				userData.getUserId();
				List<GrantedAuthority> list = new ArrayList<>();
				LOGGER.info("End - loadUserByUsername - Successful");
				return new User(userData.getUserId(), userData.getPassword(), list);
			} else {
				LOGGER.info("End - loadUserByUsername - Username Not Found");

				throw new UsernameNotFoundException("UsernameNotFoundException");
			}
		} catch (Exception e) {
			LOGGER.info("Exception - loadUserByUsername - UsernameNotFoundException");

			throw new UsernameNotFoundException("UsernameNotFoundException");
		}
	}

}
