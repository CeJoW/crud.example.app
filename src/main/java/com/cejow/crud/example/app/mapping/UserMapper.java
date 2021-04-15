package com.cejow.crud.example.app.mapping;

import com.cejow.crud.example.app.model.User;
import com.cejow.crud.example.app.model.UserDto;
import com.cejow.crud.example.app.service.VkApiClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapToUsersDro(User user, String photoLink) {
        UserDto resUser = new UserDto(user.getId(), user.getLogin(), user.getPassword(), user.getRoles(), user.getVkId());
        if (photoLink != null) {
           // VkApiClientService vkApiClientService = new VkApiClientService();
          //  String imgUrl = vkApiClientService.getImgUrl(user.getVkId(), size);
            resUser.setImgURL(photoLink);
        }
        return resUser;
    }
}
