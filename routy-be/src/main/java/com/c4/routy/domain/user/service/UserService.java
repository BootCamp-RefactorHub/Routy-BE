package com.c4.routy.domain.user.service;



import com.c4.routy.domain.user.dto.UserDTO;
import com.c4.routy.domain.user.entity.User;
import com.c4.routy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    public UserDTO findUserByEmail(UserDTO userDTO){
        String email = userDTO.getEmail();
        System.out.println("email(service) = " + email);
        User user = userRepository.findByUserEmail(email);
        if(user == null){
            return null;
        }else{
            return modelMapper.map(user, UserDTO.class);
        }
    }

    public void insertUser(UserDTO userDTO){
        System.out.println("userDTO = " + userDTO);
        userRepository.save(modelMapper.map(userDTO, User.class));
    }
}
