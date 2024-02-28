package com.example.security.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.customExceptions.ResourceNotFoundException;
import com.example.security.entities.OurUser;
import com.example.security.repository.OurUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OurUserDetailsService implements UserDetailsService {

    private final OurUserRepository ourUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return ourUserRepository.findByEmail(username).orElseThrow();
    }

    public OurUser add(OurUser ourUser) {
        Optional<OurUser> theUser = ourUserRepository.findByEmail(ourUser.getEmail());

        if(theUser.isPresent()) {
            // Deberiamos devolver una exception personalizada

            throw new ResourceNotFoundException("Ya existe un user con dicho email");
        }

        // Encriptamos la password
        ourUser.setPassword(passwordEncoder.encode(ourUser.getPassword()));
        return ourUserRepository.save(ourUser);
    }


}
