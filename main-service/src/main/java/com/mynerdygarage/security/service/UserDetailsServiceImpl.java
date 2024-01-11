package com.mynerdygarage.security.service;


import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        log.info("--- Returning UserDetails by username={}", name);

        UserDetails userDetails = userRepository.findByNameOrEmail(name, null)
                .orElseThrow(() -> new NotFoundException(String.format("- User with name=%s not found", name)));

        log.info("--- UserDetails with username={} returned", name);

        return userDetails;
    }
}
