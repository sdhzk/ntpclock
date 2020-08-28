package io.starskyoio.ntpclock.server.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Linus Lee
 * @date 2020/8/19
 */
@Data
public class NtpDateDTO {
    private String clientIP;
    private Date serverDate;
    private Date lastUpdateDate;
}
