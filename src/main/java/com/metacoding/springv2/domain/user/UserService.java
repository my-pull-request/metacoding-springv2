package com.metacoding.springv2.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.metacoding.springv2.domain.auth.AuthRequest;
import com.metacoding.springv2.domain.auth.AuthResponse;
import com.metacoding.springv2.domain.user.User;

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


//     public String 로그인(UserRequest.Login reqDTO) {
//         User user = userRepository.findByUsername(reqDTO.getUsername());

//         if (user == null) throw new RuntimeException("유저네임을 찾을 수 없습니다");

//         if (!bCryptPasswordEncoder.matches(reqDTO.getPassword(), user.getPassword()))
//             throw new RuntimeException("비밀번호가 틀렸습니다");

//         // 4. JWT 토큰 생성
//         String jwtToken = JWTUtil.create(user);

//         return jwtToken;
//     }
}