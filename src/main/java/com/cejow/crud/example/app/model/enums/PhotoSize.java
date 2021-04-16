package com.cejow.crud.example.app.model.enums;

import com.vk.api.sdk.objects.users.Fields;
import lombok.Getter;

@Getter
public enum PhotoSize {
    SMALL(Fields.PHOTO_50),
    BIG(Fields.PHOTO_200);

    private final Fields size;

    PhotoSize(Fields size) {
        this.size = size;
    }
}
