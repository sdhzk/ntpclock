package io.starskyoio.ntpclock.client.web;

import io.starskyoio.ntpclock.client.service.NtpService;
import io.starskyoio.ntpclock.client.util.NetUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = "NTPClock")
@RestController
@RequiredArgsConstructor
public class NTPClockController {

    private final NtpService ntpService;

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String version;

    @ApiOperation("同步时间")
    @GetMapping("/syncdate")
    public List<String> setSystemDate(String ntpServerIP) {
        return ntpService.syncTime(ntpServerIP);
    }

    @ApiOperation("设置ntpserver")
    @GetMapping("/setntpserver")
    public String setNtpserver(String ntpServerIP) {
        ntpService.setNtpserver(ntpServerIP);
        return "success";
    }

    @ApiOperation("系统信息")
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("app", appName);
        map.put("version", version);
        map.put("date", new Date());
        map.put("ntpClientIP", NetUtils.getLocalIpAddress());
        map.put("ntpServerIP", ntpService.getNtpserver());
        return map;
    }
}
