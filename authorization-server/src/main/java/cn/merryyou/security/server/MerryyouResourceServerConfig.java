package cn.merryyou.security.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Created on 2018/1/17.
 *
 * @author zlf
 * @since 1.0
 */
@Configuration
@EnableResourceServer
public class MerryyouResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 自定义登录成功处理器
     */
    @Autowired
    private AuthenticationSuccessHandler appLoginInSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .successHandler(appLoginInSuccessHandler)//登录成功处理器
                .and()
                .authorizeRequests().anyRequest().authenticated().and()
                .csrf().disable();
    }

}
