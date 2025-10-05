package com.example.user_srv.repository.custom;

import com.example.user_srv.entity.User;
import com.example.user_srv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class CustomUserRepository {

    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(User user) {
        userRepository.save(user);
    }
}
