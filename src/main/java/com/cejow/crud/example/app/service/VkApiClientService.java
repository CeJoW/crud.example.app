package com.cejow.crud.example.app.service;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.ServiceClientCredentialsFlowResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VkApiClientService {

    @Value("${vk.APP_ID}")
    private Integer APP_ID;
    @Value("${vk.AccessToken}")
    private String AccessToken;
    @Value("${vk.CLIENT_SECRET}")
    private String CLIENT_SECRET;

    @SneakyThrows
    public String getImgUrl(long id, PhotoSize photoSize) {
        if (id > 0) {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(transportClient);

            ServiceClientCredentialsFlowResponse authResponse = vk.oAuth()
                    .serviceClientCredentialsFlow(APP_ID, CLIENT_SECRET)
                    .execute();

            ServiceActor actor = new ServiceActor(APP_ID, AccessToken);

            List<GetResponse> getResponse = vk.users().get(actor)
                    .userIds(String.valueOf(id)).fields(photoSize.size)
                    .execute();

            String imgUrl = "";

            if (photoSize.equals(PhotoSize.SMALL))
                imgUrl = getResponse.get(0).getPhoto50().toString();
            else if (photoSize.equals(PhotoSize.BIG))
                imgUrl = getResponse.get(0).getPhoto200().toString();

            return imgUrl;
        }
        return "";
    }

    @Getter
    public enum PhotoSize {
        SMALL(Fields.PHOTO_50),
        BIG(Fields.PHOTO_200);

        private final Fields size;

        PhotoSize(Fields size) {
            this.size = size;
        }
    }
}
