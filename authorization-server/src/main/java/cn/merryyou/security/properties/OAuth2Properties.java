package cn.merryyou.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2018/1/24 0024.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "merryyou.security.oauth2")
public class OAuth2Properties {

    private String jwtSigningKey = "merryyou";

    private OAuth2ClientProperties[] clients = {};
}
