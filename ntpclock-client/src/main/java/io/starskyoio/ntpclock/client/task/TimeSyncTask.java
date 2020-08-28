package io.starskyoio.ntpclock.client.task;

import io.starskyoio.ntpclock.client.service.NtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeSyncTask {
    private final NtpService ntpService;

    @Scheduled(cron = "${task.timesync.cron}")
    public void timeSync() {
        try {
            String ntpserver = ntpService.getNtpserver();
            ntpService.syncTime(ntpserver);
        } catch (Exception e) {
            log.error("同步ntp时间失败", e);
        }

    }

    @Scheduled(cron = "${task.timeupload.cron}")
    public void timeUpload() {
        ntpService.timeUpload();
    }

    @Scheduled(cron = "${task.configsync.cron}")
    public void configSync() {
        ntpService.configSync();
    }
}

