package com.c4.routy.domain.user.service;

import com.c4.routy.domain.user.dto.UserDTO;
import com.c4.routy.domain.user.entity.UserEntity;
import com.c4.routy.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignUpServiceImpl implements SignUpService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Value("${app.default-profile-image}")
    private String defaultProfileImageUrl;

    @Autowired
    public SignUpServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    // 비밀번호 암호화 및 DB 등록 및 회원가입 완료 메시지 반환
    @Override
    public void registUser(UserDTO userDTO) {
        log.info("회원가입 서비스 메서드: {}", userDTO.getEmail());
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateUserException("이미 가입된 이메일입니다.");
        }
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        userDTO.setRole("ROLE_USER");
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setImageUrl(defaultProfileImageUrl);
        userRepository.save(userEntity);
    }
}
