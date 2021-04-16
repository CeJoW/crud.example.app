package com.cejow.crud.example.app.schedulingtasks;

import com.cejow.crud.example.app.service.PhotoCacheService;
import com.cejow.crud.example.app.service.VkApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class UsersUrlScheduledTasks {

    @Autowired
    private PhotoCacheService cache;
    @Autowired
    private VkApiClientService vkApiClientService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRateString = "${vk.job.delay}")
    public void reportCurrentTime() {
        System.out.println("Job started at" + dateFormat.format(new Date()));

        Map<Long, VkApiClientService.PhotoUrl> ex = vkApiClientService.getPhotoMap();

        cache.saveMap(ex);

        System.out.println("Job stopped at" + dateFormat.format(new Date()));
    }
}
