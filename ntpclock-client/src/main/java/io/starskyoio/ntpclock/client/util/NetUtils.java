package io.starskyoio.ntpclock.client.util;

import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Linus Lee
 * @date 2020/8/19
 */
@Slf4j
public final class NetUtils {

    public static String getLocalIpAddress() {
        return NetUtil.getLocalhost().getHostAddress();
    }
}
