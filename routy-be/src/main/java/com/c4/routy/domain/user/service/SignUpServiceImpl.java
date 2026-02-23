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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Slf4j
@Service
public class SignUpServiceImpl implements SignUpService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.default-profile-image}")
    private String defaultProfileImageUrl;

    @Autowired
    public SignUpServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, StringRedisTemplate redisTemplate) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.redisTemplate = redisTemplate;
    }

    // 비밀번호 암호화 및 DB 등록 및 회원가입 완료 메시지 반환
//    @Override
//    public void registUser(UserDTO userDTO) {
//        log.info("회원가입 서비스 메서드: {}", userDTO.getEmail());
//        if (userRepository.existsByEmail(userDTO.getEmail())) {
//            throw new DuplicateUserException("이미 가입된 이메일입니다.");
//        }
//        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
//        userDTO.setRole("ROLE_USER");
//        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
//        userEntity.setImageUrl(defaultProfileImageUrl);
//        userRepository.save(userEntity);
//    }

    @Override
    public void registUser(UserDTO userDTO){
        log.info("🔵 [회원가입 요청] 이메일: {}", userDTO.getEmail());

        // 1. Redis에서 '인증 완료' 상태인지 확인
        String isVerified = redisTemplate.opsForValue().get("VERIFIED:" + userDTO.getEmail());
        log.info("🔵 [Redis 최종 검증] 이메일: {}, 인증 상태(VERIFIED) 값: {}", userDTO.getEmail(), isVerified);

        if (!"true".equals(isVerified)) {
            log.error("🔴 [회원가입 차단] 이메일 인증 기록이 없음: {}", userDTO.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 인증이 완료되지 않았거나 인증 시간이 만료되었습니다.");
        }

        log.info("🟢 [회원가입 검증 통과] 이메일 인증 기록 확인됨. 가입 로직 진행...");

        // 2. 기존 로직: 이메일 중복 체크
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateUserException("이미 가입된 이메일입니다.");
        }

        // 3. 기존 로직: 비밀번호 암호화 및 DB 저장
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        userDTO.setRole("ROLE_USER");
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setImageUrl(defaultProfileImageUrl);
        userRepository.save(userEntity);

        // 4. 가입 완료 후, 보안을 위해 Redis에서 인증 도장 삭제 (재사용 방지)
        redisTemplate.delete("VERIFIED:" + userDTO.getEmail());
        log.info("🟢 [Redis 기록 삭제] 회원가입 완료 처리. VERIFIED 데이터 삭제됨: {}", userDTO.getEmail());
    }
}
