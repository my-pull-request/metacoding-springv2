package com.metacoding.springv2.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.metacoding.springv2.domain.auth.AuthRequest;
import com.metacoding.springv2.domain.auth.AuthResponse;
import com.metacoding.springv2.domain.user.User;
import com.metacoding.springv2.core.util.JWTUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public AuthResponse.DTO 회원가입(AuthRequest.JoinDTO requestDTO) {
        String encPassword = bCryptPasswordEncoder.encode(requestDTO.getPassword());
        User user = requestDTO.toEntity(encPassword);
        userRepository.save(user);
        return new AuthResponse.DTO(user);
    }

    public String 로그인(AuthRequest.LoginDTO requestDTO) {
        User user = userRepository.findByUsername(requestDTO.getUsername()).orElseThrow(() -> new RuntimeException("유저네임을 찾을 수 없습니다"));
        if (!bCryptPasswordEncoder.matches(requestDTO.getPassword(), user.getPassword()))
            throw new RuntimeException("비밀번호가 틀렸습니다");
        String jwtToken = JWTUtil.create(user);
        return jwtToken;
    }


}