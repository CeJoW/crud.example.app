package com.cejow.crud.example.app.service;

import com.cejow.crud.example.app.dao.UserRepository;
import com.cejow.crud.example.app.model.enums.PhotoSize;
import com.cejow.crud.example.app.model.User;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.ServiceClientCredentialsFlowResponse;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Getter
public class VkApiClientService {

    @Value("${vk.APP_ID}")
    private Integer APP_ID;
    @Value("${vk.AccessToken}")
    private String AccessToken;
    @Value("${vk.CLIENT_SECRET}")
    private String CLIENT_SECRET;
    @Value("${vk.defaultImageUrl}")
    private String defaultUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoCacheService cache;

    public Map<Long, PhotoUrl> getPhotoMap() {
        return ((List<User>) userRepository.findAll())
                .stream()
                .filter(item -> item.getVkId() > 0)
                .collect(Collectors.toMap(User::getVkId, item -> getImgUrl(item.getVkId(), PhotoSize.BIG)));
    }

    public PhotoUrl getImgUrl(long id, PhotoSize photoSize) {
        PhotoUrl imgUrl = new PhotoUrl(defaultUrl, defaultUrl);

        if (id > 0) {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(transportClient);

            try {
                ServiceClientCredentialsFlowResponse authResponse = vk.oAuth()
                        .serviceClientCredentialsFlow(APP_ID, CLIENT_SECRET)
                        .execute();
            } catch (ApiException | ClientException e) {
                return cache.getPhotoCache().getOrDefault(id, imgUrl);
            }

            ServiceActor actor = new ServiceActor(APP_ID, AccessToken);

            List<GetResponse> getResponse = null;
            try {
                getResponse = vk.users().get(actor)
                        .userIds(String.valueOf(id)).fields(photoSize.getSize())
                        .execute();
            } catch (ApiException | ClientException e) {
                return cache.getPhotoCache().getOrDefault(id, imgUrl);
            }

            String photo50, photo200;

            try {
                photo50 = getResponse.get(0).getPhoto50().toString();
            } catch (NullPointerException npe) {
                photo50 = defaultUrl;
            }

            try {
                photo200 = getResponse.get(0).getPhoto200().toString();
            } catch (NullPointerException npe) {
                photo200 = defaultUrl;
            }
            return new PhotoUrl(photo50, photo200);
        }
        return imgUrl;
    }

    @AllArgsConstructor
    @Getter
    public class PhotoUrl {
        private final String smallPhoto;
        private final String bigPhoto;
    }
}
