package com.canary.jpf.oauth.client;

import com.canary.jpf.oauth.tokenstore.RedisConfigurationProperties;
import com.canary.jpf.oauth.tokenstore.RedisTokenStoreImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;

import java.util.List;

/**
 * Created by hsk on 2017/1/11.
 */

@Configuration
@EnableResourceServer
public class OAuth2ResourceConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private RedisConfigurationProperties redisProperties;

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStoreImp("dd", redisProperties.connectionFactory());
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
