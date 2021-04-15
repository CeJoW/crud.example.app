package com.cejow.crud.example.app.service;

import com.cejow.crud.example.app.config.BCryptEncoderConfig;
import com.cejow.crud.example.app.dao.UserRepository;
import com.cejow.crud.example.app.exceptions.UsersNotFoundException;
import com.cejow.crud.example.app.mapping.UserMapper;
import com.cejow.crud.example.app.model.User;
import com.cejow.crud.example.app.model.UserDto;
import com.vk.api.sdk.objects.users.Fields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptEncoderConfig bCryptEncoderConfig;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VkApiClientService vkApiClientService;

    public List<UserDto> getUsers() {
        return ((List<User>) userRepository.findAll())
                .stream()
                .map(obj -> userMapper.mapToUsersDro(obj, vkApiClientService.getImgUrl(obj.getVkId(), VkApiClientService.PhotoSize.SMALL)))
                .collect(Collectors.toList());
    }

//    public List<UserDto> getUsers() {
//        return ((List<User>) userRepository.findAll())
//                .stream()
//                .map(obj -> UserMapper.mapToUsersDro(obj, Fields.PHOTO_50))
//                .collect(Collectors.toList());
//    }

//    public UserDto getUser(long id) {
//        return UserMapper.mapToUsersDro(userRepository.findById(id).orElseThrow(() -> new UsersNotFoundException(id)), Fields.PHOTO_100);
//    }

    public UserDto getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsersNotFoundException(id));
        return userMapper.mapToUsersDro(user, vkApiClientService.getImgUrl(user.getVkId(), VkApiClientService.PhotoSize.BIG));
    }

    public User addUser(User user) {
        user.setPassword(bCryptEncoderConfig.passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public String doRegister(User user) {
        if (userRepository.findByLoginIs(user.getLogin()).isPresent()) {
            return "User exists!";
        }
        addUser(user);
        return "success";
    }
}
