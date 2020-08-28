package io.starskyoio.ntpclock.server.web;

import io.starskyoio.ntpclock.server.dto.NtpDateDTO;
import io.starskyoio.ntpclock.server.service.NtpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Linus Lee
 * @date 2020/8/19
 */
@Api(tags = "NTP时间")
@RestController
@RequiredArgsConstructor
public class NtpController {

    private final NtpService ntpService;

    @ApiOperation("上传ntp客户端时间")
    @PostMapping("/upload")
    public String uploadTime(@RequestBody NtpDateDTO timeDTO) {
        ntpService.update(timeDTO);
        return "success";
    }

    @ApiOperation("获取ntp客户端时间")
    @GetMapping("/date")
    public Map<String, NtpDateDTO> getNtpClientDate() {
        return ntpService.getNtpClientDate();
    }

    @ApiOperation("获取ntp服务器ip")
    @GetMapping("/config")
    public String getNTPServerIP() {
        return ntpService.getNtpServerIP();
    }

    @ApiOperation("设置ntp服务器ip")
    @PutMapping("/config")
    public String setNTPServerIP(String ip) {
        boolean flag = ntpService.setNtpServerIP(ip);
        return flag ? "success" : "fail";
    }
}
