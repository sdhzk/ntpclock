package io.starskyoio.ntpclock.server.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import io.starskyoio.ntpclock.server.dto.NtpDateDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ntp客户端时间
 *
 * @author Linus Lee
 * @date 2020/8/19
 */
@Service
public class NtpService {

    private static final String NTP_SERVER_IP = "NTP_SERVER_IP";

    private static final Map<String, String> CONFIG = Maps.newConcurrentMap();

    private static final Cache<String, NtpDateDTO> TIME_CACHE = CacheBuilder.newBuilder()
            .initialCapacity(32)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();


    @Value("${ntpserver.default.ip}")
    private String ntpServerIP;

    @PostConstruct
    public void init() {
        CONFIG.put(NTP_SERVER_IP, ntpServerIP);
    }

    /**
     * 更新ntp客户端时间
     *
     * @param ntpDateDTO
     */
    public void update(NtpDateDTO ntpDateDTO) {
        if (StringUtils.isBlank(ntpDateDTO.getClientIP())) {
            return;
        }
        if (!InetAddressValidator.getInstance().isValidInet4Address(ntpDateDTO.getClientIP())) {
            return;
        }
        ntpDateDTO.setLastUpdateDate(new Date());
        TIME_CACHE.put(ntpDateDTO.getClientIP(), ntpDateDTO);
    }

    /**
     * 获取ntp客户端时间
     *
     * @return
     */
    public Map<String, NtpDateDTO> getNtpClientDate() {
        return TIME_CACHE.asMap();
    }

    public String getNtpServerIP() {
        return CONFIG.get(NTP_SERVER_IP);
    }

    public boolean setNtpServerIP(String ip){
        if(StringUtils.isBlank(ip)){
            return false;
        }
        if (!InetAddressValidator.getInstance().isValidInet4Address(ip)) {
            return false;
        }
        CONFIG.put(NTP_SERVER_IP, ip);
        return true;
    }

}
