package com.c4.routy.domain.plandraw.service;


import com.c4.routy.common.util.DateTimeUtil;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plandraw.dto.PlanCreateRequestDTO;
import com.c4.routy.domain.plandraw.dto.PlanResponseDTO;
import com.c4.routy.domain.plandraw.repository.PlanDrawRepository;
import com.c4.routy.domain.region.entity.RegionEntity;
import com.c4.routy.domain.region.repository.RegionRepository;
import com.c4.routy.domain.user.entity.UserEntity;
import com.c4.routy.domain.user.repository.UserRepository;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service("PlanDrawService")
@RequiredArgsConstructor
public class PlanDrawService {

    private final ModelMapper modelMapper;
    private final PlanDrawRepository planRepository;
    private final RegionRepository RegionRepository;
    private final UserRepository userRepository;


    /**
     * 일정 생성 시 Duration(일차) 자동 생성
     */
    @Transactional
    public PlanEntity createPlan(PlanCreateRequestDTO dto) {

        /**
         * Spring Security를 사용해 현재 로그인한 사용자의 정보를 가져오는 과정
         *
         * 1. SecurityContextHolder.getContext().getAuthentication()
         *      SecurityContextHolder: 현재 애플리케이션의 보안 컨텍스트를 저장하고 관리하는 저장소로 현재 실행 중인 스레드의 보안 정보를 저장
         *      getContext(): 현재 스레드에 저장된 SecurityContext 객체를 반환
         *      getAuthentication: SecurityContext객체의 Authentication객체 반환
         *          Authentication객체: 현재 로그인한 사용자의 인증 정보를 담고 있는 인터페이스
         *                              로그인하지 않았다면 null이거나 AnonymousAuthenticationToken일 수 있음
         * 2. (CustomUserDetails) auth.getPrincipal()
         *      auth.getPrincipal(): Authentication 객체에서 주체(Principal)를 반환
         *          Principal: 인증된 사용자를 식별하는 정보. 보통 UserDetails 인터페이스를 구현한 객체
         *
         * 이렇게 필터를 이용해서 하게되면 매개변수로 토큰을 받을 필요도 없으며, 보안/인증 로직을 분리가능함
         * 또한 인증 방식이 변경되는 경우 필터만 수정해주면 되므로 유지보수성이 좋아진다
         *      => 특수한 경우(로그인/재발급 API, 여러 토큰 사용(Access Token 외 별도 검증 토큰 또는 일회성 토큰))를 제외하고 일반적으로 로그인한 사용자의 정보를 필요로 하는 API들은 이렇게 인증 객체를 꺼내 쓰는 것이 표준적인 패턴
         *
         *
         * TODO: 생각나는대로 적기
         *  1. 다른 인증 토큰의 값을 사용하는 메소드도 이런 방식으로 변경
         *  2. Controller 단에서의 반환값 통일하기 (CustomResponse 만들어서 하기)
         *  3. Exception처리 (BusinessException 클래스와 예외 코드 Enum 타입)
         */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Integer userNo = userDetails.getUserNo();

        // PlanEntity 생성 및 저장
        PlanEntity plan = new PlanEntity();
        plan.setPlanTitle(dto.getPlanTitle());
        plan.setStartDate(dto.getStartDate());
        plan.setEndDate(dto.getEndDate());
        plan.setTheme(dto.getTheme());
        //  region_id -> RegionEntity 로 변환해서 주입
        RegionEntity region = RegionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("지역 없음"));
        plan.setRegion(region);

        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        plan.setUser(user);

        plan.setCreatedAt(DateTimeUtil.now());

        PlanEntity savedPlan = planRepository.save(plan);

        return savedPlan;
    }

    // 전체 플랜 조회
    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PlanResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Plan 단건 조회
    public PlanResponseDTO getPlanById(Integer planId) {
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        return modelMapper.map(plan, PlanResponseDTO.class);
    }

    // 사용자별 플랜 조회
    public List<PlanResponseDTO> getPlansByUser(Integer userNo) {
        return planRepository.findByUser_UserNo(userNo)
                .stream()
                .map(p -> modelMapper.map(p, PlanResponseDTO.class))
                .collect(Collectors.toList());
    }
}