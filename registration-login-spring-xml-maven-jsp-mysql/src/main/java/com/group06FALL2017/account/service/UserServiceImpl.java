package com.group06FALL2017.account.service;

import com.group06FALL2017.account.model.User;
import com.group06FALL2017.account.repository.RoleRepository;
import com.group06FALL2017.account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEmail(bCryptPasswordEncoder.encode(user.getEmail()));
        user.setAddress(bCryptPasswordEncoder.encode(user.getAddress()));
        user.setStatus(bCryptPasswordEncoder.encode(user.getStatus()));
        user.setDOB(bCryptPasswordEncoder.encode("1993-12-11"));
        user.setPhone(bCryptPasswordEncoder.encode("4806528693"));
        
//        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
