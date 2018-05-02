package cn.merryyou.security.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2018/1/24 0024.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
public class OAuth2CoreConfig {
}
