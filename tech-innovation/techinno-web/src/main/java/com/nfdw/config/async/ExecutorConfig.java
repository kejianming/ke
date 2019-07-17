package com.nfdw.config.async;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 * @Description: 线程池配置类
 * @Author Ivan Lee
 * @Date 2018年5月21日
 */
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "taskthreadpool", ignoreUnknownFields = false)
public class ExecutorConfig {

	/**
	 * 线程池维护线程的最小数量
	 */
    private int corePoolSize;

    /**
     * 线程池维护线程的最大数量
     */
    private int maxPoolSize;

    /**
     * 线程池所使用的缓冲队列数量
     */
    private int queueCapacity;
    
    /**
     * 线程池维护线程所允许的空闲时间
     */
    private int keepAliveSeconds;
    
    public int getCorePoolSize() {
		return corePoolSize;
	}
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	public int getMaxPoolSize() {
		return maxPoolSize;
	}
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	public int getQueueCapacity() {
		return queueCapacity;
	}
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}
	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}
	
	/**
     * 此线程池的策咯为：CallerRunsPolicy（在任务被拒绝添加后，会调用当前线程池的所在的线程去执行被拒绝的任务）
     * @return
     */
    @Bean
    public Executor serverMonitorAsyncExecutor() {
    	Executor executor = getAsyncExecutor(corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds, "serverMonitorAsyncExecutor-", new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
    
    private Executor getAsyncExecutor(int corePoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds, String threadNamePrefix, RejectedExecutionHandler rejectedExecutionHandler) {
    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler(rejectedExecutionHandler);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.initialize();
        return executor;
    }
}
