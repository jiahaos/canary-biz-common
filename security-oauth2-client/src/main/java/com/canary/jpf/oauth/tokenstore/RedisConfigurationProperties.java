package com.canary.jpf.oauth.tokenstore;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;

import java.util.List;

/**
 * @author jiahao
 * @Package com.canary.security.oauth.server
 * @Description: redis配置模型
 * @date 2017/11/7 16:17
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisConfigurationProperties {

    List<String> nodes;

    String requirepass;

    int nodeSize;


    /**
     * 封装redis连接
     * @return
     */
    public TokenStore tokenStore() {
        return new RedisTokenStoreImp("dd", connectionFactory());
    }

    /**
     * 封装redis连接工厂
     * @return
     */
    public RedisConnectionFactory connectionFactory() {
        //单点
        if(nodes.size() == 1) {
            JedisShardInfo shardInfo = new JedisShardInfo(nodes.get(0), Protocol.DEFAULT_PORT);
            shardInfo.setPassword(requirepass);
            return new JedisConnectionFactory(shardInfo);
        }
        //集群
        return new JedisConnectionFactory(
                new RedisClusterConfiguration(nodes));
    }

    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore());
        return tokenServices;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
        this.nodeSize = nodes.size();
    }

    public int getNodeSize() {
        return this.nodeSize;
    }

}
