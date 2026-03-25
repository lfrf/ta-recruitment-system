package com.group27.tarecruitment.service;
import com.group27.tarecruitment.model.UserAccount; import com.group27.tarecruitment.repository.UserRepository; import java.util.Optional;
public class AuthService { private final UserRepository userRepository = new UserRepository(); public Optional<UserAccount> authenticate(String username, String password){ return userRepository.findByUsername(username).filter(UserAccount::isActive).filter(user -> user.getPassword()!=null && user.getPassword().equals(password)); } }
