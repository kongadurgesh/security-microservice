package com.tprm.security.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tprm.security.dto.UserDTO;
import com.tprm.security.entity.User;
import com.tprm.security.exception.UnauthorizedException;
import com.tprm.security.repository.UserRepository;
import com.tprm.security.utils.AuthenticationResponse;
import com.tprm.security.utils.Role;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    public AuthenticationResponse register(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(UserDTO userDTO) throws UnauthorizedException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
        if (user.isEmpty()) {
            throw new UnauthorizedException("User Access Forbidden...");
        }
        String jwtToken = jwtService.generateToken(user.get());
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
