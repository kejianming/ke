package com.nfdw.config.redis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
//@EnableAutoConfiguration
public class RedisConfig {
    private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    
    @Value("${spring.redis.password}")
    private String password;
    
    @Value("${spring.redis.jedis.pool.max-total}")
    private int maxTotal;
    
    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWait;
    
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    
    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;
    
    @Value("${spring.redis.jedis.pool.testWhileIdle}")
    private boolean testWhileIdle;
    
    @Value("${spring.redis.jedis.pool.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;
    
    @Value("#{'${spring.redis.sentinel.nodes}'.split(',')}")
    private List<String> nodes;

    @Bean
    public JedisPoolConfig getRedisConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWait);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestWhileIdle(testWhileIdle);
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        return config;
    }

	@Bean
	public RedisSentinelConfiguration sentinelConfiguration() {
		RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
		// 配置matser的名称
		redisSentinelConfiguration.master("mymaster");
		// 配置redis的哨兵sentinel
		Set<RedisNode> redisNodeSet = new HashSet<>();
		nodes.forEach(x -> {
			redisNodeSet.add(new RedisNode(x.split(":")[0], Integer.parseInt(x.split(":")[1])));
		});
		logger.info("redisNodeSet -->" + redisNodeSet);
		redisSentinelConfiguration.setSentinels(redisNodeSet);
		
		redisSentinelConfiguration.setPassword(RedisPassword.of(password));
		return redisSentinelConfiguration;
	}

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig,RedisSentinelConfiguration sentinelConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig,jedisPoolConfig);
        return jedisConnectionFactory;
    }
    
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(JedisConnectionFactory redisConnectionFactory) {
		// StringRedisTemplate的构造方法中默认设置了stringSerializer
		RedisTemplate<Object, Object> template = new RedisTemplate<>();

		template.setConnectionFactory(redisConnectionFactory);

		// 设置开启事务
		template.setEnableTransactionSupport(true);

		// set key serializer
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);

		template.afterPropertiesSet();

		return template;
	}
    
}
