package io.starskyoio.ntpclock.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Linus Lee
 * @date 2020/8/27
 */
@SpringBootApplication
public class NtpClockServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NtpClockServerApplication.class, args);
    }
}
