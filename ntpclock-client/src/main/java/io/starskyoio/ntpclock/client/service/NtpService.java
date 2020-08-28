package io.starskyoio.ntpclock.client.service;

import io.starskyoio.ntpclock.client.dto.NTPTimeDTO;
import io.starskyoio.ntpclock.client.util.NetUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class NtpService {
    private static final String NTP_SERVER = "NTP_SERVER";

    private final Map<String, String> config = new ConcurrentHashMap<>();

    @Value("${config.server.url}")
    private String configServerUrl;

    @Value("${upload.server.url}")
    private String uploadServerUrl;

    private final RestTemplate restTemplate;

    /**
     * 执行命令 "bin/ntpdate -bu 172.17.66.30"
     * @param cmd
     * @return
     * @throws IOException
     */
    private List<String> runCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            List<String> lines = IOUtils.readLines(new BufferedReader(new InputStreamReader(process.getInputStream())));
            lines.forEach(log::info);
            return lines;
        } catch (IOException e){
            log.error("runCmd failed", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 同步ntp时间
     * @param ip ntp服务器ip
     * @return
     */
    public List<String> syncTime(String ip){
        if(StringUtils.isBlank(ip)){
            log.warn("ntp服务器ip为空");
            return Collections.singletonList("ntp服务器ip为空");
        }

        return runCmd("bin/ntpdate -bu " + ip);
    }


    /**
     * 设置ntp服务器ip
     * @param ip
     */
    public void setNtpserver(String ip) {
        config.put(NTP_SERVER, ip);
        log.info("设置ntp服务器ip为{}", ip);
    }

    /**
     * 查询ntp服务器ip
     * @return
     */
    public String getNtpserver() {
        return config.get(NTP_SERVER);
    }

    public void configSync() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(configServerUrl, String.class);
            if(response.getStatusCodeValue() != HttpStatus.OK.value()){
                log.warn("从服务器获取ntp配置失败，请求失败code：" + response.getStatusCodeValue());
                return;
            }
            String ntpserver = response.getBody();
            if(StringUtils.isBlank(ntpserver)){
                log.warn("从服务器获取ntp配置失败，ntp服务器ip为空");
                return;
            }
            setNtpserver(ntpserver);
            log.info("从服务器获取ntp配置成功");
        } catch (Exception e){
            log.error("从服务器获取ntp配置失败，{}", e.getMessage());
        }
    }

    public void timeUpload() {
        try {
            NTPTimeDTO timeDTO = new NTPTimeDTO();
            timeDTO.setClientIP(NetUtils.getLocalIpAddress());
            timeDTO.setServerDate(new Date());
            ResponseEntity<String> response = restTemplate.postForEntity(uploadServerUrl, timeDTO, String.class);
            if(response.getStatusCodeValue() != HttpStatus.OK.value()){
                log.warn("上传ntp客户端时间失败，请求失败code：" + response.getStatusCodeValue());
                return;
            }
            String result = response.getBody();
            if("success".equals(result)){
                log.info("上传ntp客户端时间成功: {}", timeDTO);
            }else{
                log.warn("上传ntp客户端时间失败: {}", timeDTO);
            }
        } catch (Exception e){
            log.error("上传ntp客户端时间失败，{}", e.getMessage());
        }
    }
}
