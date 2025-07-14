package com.classycollections.inventory_bookkeeping.auth;

import com.classycollections.inventory_bookkeeping.user.User;
import com.classycollections.inventory_bookkeeping.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userRepo.findByEmail(request.email()).orElseThrow();
        var token = jwtService.generateToken(user.getEmail());
        return new AuthenticationResponse(token);
    }

    public void register(RegisterRequest request) {
        if (userRepo.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .enabled(true)
                .build();
        userRepo.save(user);
    }
}