package com.c4.routy.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.c4.routy.domain.user.dto.RequestChangePwdDTO;
import com.c4.routy.domain.user.dto.RequestModifyUserInfoDTO;
import com.c4.routy.domain.user.entity.UserEntity;
import com.c4.routy.domain.user.mapper.AuthMapper;
import com.c4.routy.domain.user.repository.UserRepository;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.TimeUnit;
import java.util.Random;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${folder.profile}")
    private String folder;  // application.yml에 있는 폴더

    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthMapper authMapper;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AuthServiceImpl(AmazonS3 amazonS3,
                           UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, AuthMapper authMapper, StringRedisTemplate redisTemplate) {
        this.amazonS3 = amazonS3;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authMapper = authMapper;
        this.redisTemplate = redisTemplate;
    }

    // UserDetailsService에 의한 로그인을 위한 DB 조회용 메서드
    // provider에서 userService가 호출하는 메서드
    // 스프링 시큐리티 사용 시 프로바이더에서 활용할 로그인용 메서드(UserDetails 타입을 반환하는 메서드)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

//        // 쿼리 메소드를 활용한 아이디 where절을 활용
//        UserEntity loginUser = userRepository.findByEmail(email);

        UserEntity loginUser;

        if (email.matches("\\d+")) { // 숫자면 user_no로 조회
            Integer userNo = Integer.parseInt(email);
            loginUser = userRepository.findById(userNo)
                    .orElseThrow(() -> new UsernameNotFoundException(userNo + "번 회원은 존재하지 않습니다."));
        } else {
            loginUser = userRepository.findByEmail(email);
        }

        // 사용자가 로그인 시 아이디를 잘못 입력했다면
        if(loginUser == null) {
            throw new UsernameNotFoundException(email + "아이디의 회원은 존재하지 않습니다.");
        }

        if(loginUser.isDeleted()) {
            throw new UsernameNotFoundException("탈퇴한 회원입니다.");
        }

        // DB에서 조회된 해당 아이디의 회원이 가진 권한들을 가져와 List<GrandtedAuthority>로 전환
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(loginUser.getRole()));
        return new CustomUserDetails(loginUser);
    }

    // 회원 번호로 UserDetails 조회 (JWT 토큰 검증용)
    public UserDetails loadUserByUserNo(Integer userNo) throws UsernameNotFoundException {

        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new UsernameNotFoundException(userNo + "번 회원은 존재하지 않습니다."));

        if(user.isDeleted()) {
            throw new UsernameNotFoundException("탈퇴한 회원입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new CustomUserDetails(user);
    }

    // 로그아웃 처리
    // 1. HttpOnly 쿠키 삭제
    // 2. SecurityContext 초기화
    @Override
    public void logout(HttpServletResponse response) {

        // 현재 인증된 사용자 정보 로깅
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            log.info("로그아웃 처리 시작 - 사용자: {}", username);
        }

        // HttpOnly 쿠키 삭제
        deleteCookie(response);

        // SecurityContext 초기화
        SecurityContextHolder.clearContext();

        log.info("로그아웃 처리 완료 - 쿠키 삭제 및 SecurityContext 초기화");
    }

    // 현재 인증 상태 확인
    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    //현재 인증된 사용자 이름 반환
    /* 현재 이 함수가 사용되어지는 곳이 거의 없다 이 함수를 사용할 수 있는 부분을 찾아서 사용해보자
       단. 이메일만 사용하는 함수일 경우에만 사용할 수 있으니 주의하자 */
//    @Override
//    public String getCurrentUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            return authentication.getName();
//        }
//
//        return null;
//    }

    // HttpOnly 쿠키 삭제 (private helper method)
    private void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");  // 홈화면으로
        cookie.setMaxAge(0);  // 즉시 만료
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);
        log.info("HttpOnly 쿠키 삭제 완료");
    }

    // 비밀번호 변경
    @Override
    public void modifyPwd(RequestChangePwdDTO newPwd) {

        // 1. 비밀번호 유효성 검사
        if (newPwd.getNewPassword() == null || newPwd.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }

        // 2. 사용자 조회
        /* 지금 이런 식이면 클라이언트 즉 프론트 엔드 API 버그로 인해 로그인한 사람 외에 이메일이 넘어가는 경우가 발생
           이럴 경우 다른 사람의 비밀번호를 변경할 수 잇는 문제 발생 getCurrentUsername() 함수 사용 권장
           ex) 로그아웃 후 다른 이메일로 로그인 했는데 이전 로그인한 계정으로 넘어갈 수 있는 버그 발생에 대비*/
        UserEntity user = userRepository.findByEmail(newPwd.getEmail());

        // 3. 새 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(newPwd.getNewPassword());

        // 4. 비밀번호 변경
        user.setPassword(encodedPassword);

        log.info("비밀번호 변경 완료 - 이메일: {}", user.getEmail());

        // 5. DB 저장
        userRepository.save(user);
    }

    // 회원정보 수정
    @Override
    public String modifyUserInfo(RequestModifyUserInfoDTO newUserInfo, Integer userNo, MultipartFile profile) {
        UserEntity userInfo = userRepository.findById(userNo).get();

        if(newUserInfo.getUsername() != null) {
            userInfo.setUsername(newUserInfo.getUsername());
        }
        if(newUserInfo.getAge() != null) {
            userInfo.setAge(Integer.parseInt(newUserInfo.getAge()));
        }
        if(newUserInfo.getPhone() != null) {
            userInfo.setPhone(newUserInfo.getPhone());
        }

        if (profile != null) {
            String url = userInfo.getImageUrl();
            if(url.contains("routy-service")) {
                String key =  url.substring(url.indexOf(".amazonaws.com/") + ".amazonaws.com/".length());
                log.info("key1 : {}", key);
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
            }
            log.info("key2: {}", url);
            String fileName = createFileName(profile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(profile.getSize());
            objectMetadata.setContentType(profile.getContentType());

            try(InputStream inputStream = profile.getInputStream()){
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                // 접근 가능한 URL 반환
                userInfo.setImageUrl(amazonS3.getUrl(bucket, fileName).toString());
            } catch (IOException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
        }

        userRepository.save(userInfo);
        return "회원정보가 수정되었습니다.";
    }

    // S3 객체 키(사진이름) 만들기 (application.yml에서 폴더 이름 더해야 함.)
    public String createFileName(String fileName){
        return folder + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }
    // 확장자
    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
        }
    }

    // 이메일 찾기 및 유효성 검사
    @Override
    public String findEmail(String username, String phone) {

        String email = authMapper.findEmailByUsernameAndPhone(username, phone);

        // 사용자를 찾지 못한 경우
        if (email == null || email.isEmpty()) {
            return "존재하지 않는 회원입니다.";
        }

        // 이메일을 찾은 경우 그대로 반환
        return email;
    }

    // Redis를 통한 이메일 인증 방식
    // 1. 인증번호 생성 및 Redis 저장
    public void sendVerificationCode(String email){
        // 6자리 난수 생성
        String code = String.format("%06d", new Random().nextInt(1000000));

        // Redis에 저장: Key는 "AUTH_CODE:이메일", Value는 "인증번호", 유효기간 "3분"
        redisTemplate.opsForValue().set("AUTH_CODE:" + email, code, 3, TimeUnit.MINUTES);

        log.info("🟢 [Redis 발송 저장] 이메일: {}, 발급된 인증번호: {} (3분 유효)", email, code);
    }

    // 2. 인증번호 확인 및 '인증 완료' 도장 찍기
    public boolean confirmVerificationCode(String email, String code){
        // Redis에서 저장된 인증번호 꺼내기
        String savedCode = redisTemplate.opsForValue().get("AUTH_CODE:" + email);

        log.info("🔵 [Redis 검증 시도] 이메일: {}, 입력코드: {}, 저장된코드: {}", email, code, savedCode);

        // 저장된 코드가 존재하고, 입력한 코드와 일치하는지 확인
        if(savedCode != null && savedCode.equals(code)){
            // 1) 성공했으니 기존 인증번호 임시 데이터는 삭제
            redisTemplate.delete(("AUTH_CODE:" + email));

            // 2) '인증 완료'라는 새로운 기록을 10분간 저장(회원 가입할 때까지의 여유 시간)
            redisTemplate.opsForValue().set("VERIFIDE:" + email, "true", 10, TimeUnit.MINUTES);

            log.info("🟢 [Redis 인증 성공] 이메일: {} -> 'VERIFIED' 상태로 10분간 저장 완료!", email);
            return true;
        }

        log.warn("🔴 [Redis 인증 실패] 이메일: {} -> 번호가 틀렸거나 3분이 지나 만료됨", email);
        return false;
    }
}
