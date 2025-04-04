package com.example.SpringSecurity.service;

import com.example.SpringSecurity.dto.JoinDto;
import com.example.SpringSecurity.entity.UserEntity;
import com.example.SpringSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto) {

        // username 중복 검사
        boolean isUser = userRepository.existsByUsername(joinDto.getUsername());
        if (isUser) return;

        UserEntity data = new UserEntity();
        data.setUsername(joinDto.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword())); // 비밀번호 암호화
        data.setRole("ROLE_USER");
        userRepository.save(data);
    }
}
