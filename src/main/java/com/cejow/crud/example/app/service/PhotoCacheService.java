package com.cejow.crud.example.app.service;

import com.cejow.crud.example.app.dao.UserRepository;
import com.cejow.crud.example.app.model.enums.PhotoSize;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Getter
public class PhotoCacheService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VkApiClientService vkApiClientService;

    private Map<Long, VkApiClientService.PhotoUrl> photoCache;

    public void saveMap(Map<Long, VkApiClientService.PhotoUrl> map) {
        this.photoCache = map;
    }

    public String getPhotoUrl(long id, PhotoSize size) {
        if (id > 0 && photoCache.containsKey(id)) {
            if (size.equals(PhotoSize.SMALL)) {
                return photoCache.get(id).getSmallPhoto();
            } else {
                return photoCache.get(id).getBigPhoto();
            }
        }

        return vkApiClientService.getDefaultUrl();
    }
}
