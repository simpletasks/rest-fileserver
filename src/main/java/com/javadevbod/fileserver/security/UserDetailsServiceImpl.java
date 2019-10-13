package com.javadevbod.fileserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.javadevbod.fileserver.data.jpa.entities.User;
import com.javadevbod.fileserver.data.jpa.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByUsername(email);

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        user.setRoles(new String[] { "PRIVATE" });

        return new UserPrincipal(user);
    }
}
