package com.canary.jpf.oauth.client;

import com.canary.jpf.oauth.tokenstore.RedisConfigurationProperties;
import com.canary.jpf.oauth.tokenstore.RedisClusterTokenStoreImp;
import com.canary.jpf.oauth.tokenstore.RedisTokenStoreImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class OAuth2ResourceConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private RedisConfigurationProperties redisProperties;

    @Bean
    public TokenStore tokenStore() {
        if(redisProperties.getNodeSize() == 1)
            return new RedisTokenStoreImp("dd", redisProperties.connectionFactory());
        return new RedisClusterTokenStoreImp("dd", redisProperties.connectionFactory());
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.resourceId("restservice").tokenServices(redisProperties.tokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/open").hasAnyAuthority("USER");
        //  .antMatcher("/**")
        // Add below
        // .authorizeRequests()
        // .anyRequest().authenticated();
    }

}
