package com.nfdw.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.nfdw.DataSourceType;
import com.nfdw.holder.DynamicDataSource;

import tk.mybatis.mapper.autoconfigure.SpringBootVFS;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @Description: 数据库连接池、动态多数据源配置
 * @Author Ivan Lee
 * @Date 2019年4月16日
 */
@Configuration
//@EnableTransactionManagement
@EnableConfigurationProperties(MybatisProperties.class)
public class DruidConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DruidConfig.class);
    
	/**
	 * druid config
	 */
	
	@Value("${spring.datasource.druid.type}")
    private String type;
	@Value("${spring.datasource.druid.filters}")
    private String filters;
	@Value("${spring.datasource.druid.maxActive}")
	private int maxActive;
	@Value("${spring.datasource.druid.initialSize}")
    private int initialSize;
	@Value("${spring.datasource.druid.maxWait}")
    private int maxWait;
	@Value("${spring.datasource.druid.minIdle}")
    private int minIdle;
	@Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
	@Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
	@Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;
	@Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;
	@Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;
	@Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;
	@Value("${spring.datasource.druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;
	@Value("${spring.datasource.druid.maxOpenPreparedStatements}")
    private int maxOpenPreparedStatements;
	@Value("${spring.datasource.druid.web-stat-filter.enabled}")
    private String enabled;
	@Value("${spring.datasource.druid.web-stat-filter.url-pattern}")
    private String urlPattern;
	@Value("${spring.datasource.druid.web-stat-filter.exclusions}")
    private String exclusions;
	@Value("${spring.datasource.druid.web-stat-filter.session-stat-enable}")
    private String sessionStatEnable;
	@Value("${spring.datasource.druid.web-stat-filter.session-stat-max-count}")
    private String sessionStatMaxCount;
	@Value("${spring.datasource.druid.web-stat-filter.principal-session-name}")
    private String principalSessionName;
	@Value("${spring.datasource.druid.web-stat-filter.profile-enable}")
    private String profileEnable;
	@Value("${spring.datasource.druid.stat-view-servlet.enabled}")
    private String statViewServletEnabled;
	@Value("${spring.datasource.druid.stat-view-servlet.url-pattern}")
    private String statViewServletUrlPattern;
	@Value("${spring.datasource.druid.stat-view-servlet.reset-enable}")
    private String resetEnable;
	@Value("${spring.datasource.druid.aop-patterns}")
    private String aopPatterns;
	
	/**
	 * 第一数据源配置
	 */
	@Value("${spring.datasource.primary.driver-class-name}")
    private String primaryDriverClassName;
	@Value("${spring.datasource.primary.url}")
    private String primaryUrl;
    @Value("${spring.datasource.primary.username}")
    private String primaryUsername;
    @Value("${spring.datasource.primary.password}")
    private String primaryPassword;
    
    /**
	 * 第一数据源配置
	 */
	@Value("${spring.datasource.secondary.driver-class-name}")
    private String secondaryDriverClassName;
	@Value("${spring.datasource.secondary.url}")
    private String secondaryUrl;
    @Value("${spring.datasource.secondary.username}")
    private String secondaryUsername;
    @Value("${spring.datasource.secondary.password}")
    private String secondaryPassword;
    
    private MybatisProperties mybatisProperties;
    
    public DruidConfig(MybatisProperties properties) {
        this.mybatisProperties = properties;
    }
    
    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(primaryUrl);
        datasource.setUsername(primaryUsername);
        datasource.setPassword(primaryPassword);
        datasource.setDriverClassName(primaryDriverClassName);
        this.setDruidOptions(datasource); // 设置druid数据源的属性
        
        return datasource;
    }
    
    @Bean(name = "secondaryDataSource")
    public DataSource secondaryDataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(secondaryUrl);
        datasource.setUsername(secondaryUsername);
        datasource.setPassword(secondaryPassword);
        datasource.setDriverClassName(secondaryDriverClassName);
        
        this.setDruidOptions(datasource);
        
        return datasource;
    }
    
    @Bean(name="dynamicDataSource")
    @Primary    //优先使用，多数据源
    public DynamicDataSource dataSource(@Qualifier("primaryDataSource") DataSource primaryDataSource, @Qualifier("secondaryDataSource") DataSource secondaryDataSource) {
//        DataSource primary = primaryDataSource();
//        DataSource secondary = secondaryDataSource();
        //设置默认数据源
        //配置多数据源
        Map<Object,Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.Primary.getName(), primaryDataSource);
        targetDataSources.put(DataSourceType.Secondary.getName(), secondaryDataSource);
        
        return new DynamicDataSource(primaryDataSource, targetDataSources);
    }
    
	/**
	 * 配置mybatis的sqlSession连接动态数据源
	 * 
	 * @param dynamicDataSource
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dynamicDataSource);
		bean.setMapperLocations(mybatisProperties.resolveMapperLocations());
		bean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
		bean.setConfiguration(mybatisProperties.getConfiguration());
		bean.setVfs(SpringBootVFS.class);
		return bean.getObject();
	}

	@Bean(name = "sqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
    
    /**
     * 事务管理
     * @return
     */
    @Bean
	public PlatformTransactionManager txManager(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
		return new DataSourceTransactionManager(dynamicDataSource);
	}
    
    @Bean
    public FilterRegistrationBean druidWebStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("enabled", enabled);
        filterRegistrationBean.addInitParameter("url-pattern", urlPattern);
        filterRegistrationBean.addInitParameter("exclusions", exclusions);
        filterRegistrationBean.addInitParameter("session-stat-enable", sessionStatEnable);
        filterRegistrationBean.addInitParameter("session-stat-max-count", sessionStatMaxCount);
        filterRegistrationBean.addInitParameter("principal-session-name", principalSessionName);
        filterRegistrationBean.addInitParameter("profile-enable", profileEnable);

//        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
//        proxy.setTargetFilterLifecycle(true);
//        proxy.setTargetBeanName("shiroFilter");
//
//        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }
    
    @Bean(name = "shiroFilterRegistrationBean")
    public FilterRegistrationBean shiroFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");

        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }
    

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings(statViewServletUrlPattern);
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("reset-enable", resetEnable);
        initParameters.put("allow", "");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

	@Bean
	public DruidStatInterceptor getDruidStatInterceptor(){
	  return new DruidStatInterceptor();
	}
	
	@Bean
	@Scope("prototype")
	public JdkRegexpMethodPointcut getJdkRegexpMethodPointcut(){
	  JdkRegexpMethodPointcut pointcut=new JdkRegexpMethodPointcut();
	  String[] str={"com.nfdw.service.*","com.nfdw.mapper.*"};
	  pointcut.setPatterns(str);
	  return pointcut;
	}
	
	@Bean
	public WallFilter wallFilter(){

		WallFilter wallFilter=new WallFilter();
		wallFilter.setConfig(wallConfig());
		
		return wallFilter;

	}
	
	@Bean
	public WallConfig wallConfig(){

		WallConfig config =new WallConfig();
		config.setMultiStatementAllow(true);//允许一次执行多条语句
		config.setNoneBaseStatementAllow(true);//允许非基本语句的其他语句
		return config;

	}
	/*@Bean
	public StatFilter statFilter(){

	  StatFilter statFilter = new StatFilter();
	  return statFilter;

	}*/
	private void setDruidOptions(DruidDataSource datasource){
//		datasource.setDbType(type);
		datasource.setMaxActive(maxActive);
		datasource.setInitialSize(initialSize);
		datasource.setMaxWait(maxWait);
		datasource.setMinIdle(minIdle);
		datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		datasource.setValidationQuery(validationQuery);
		datasource.setTestWhileIdle(testWhileIdle);
		datasource.setTestOnBorrow(testOnBorrow);
		datasource.setTestOnReturn(testOnReturn);
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		datasource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
		
		try {
			datasource.setFilters(filters);
		} catch (SQLException e) {
			LOGGER.error("druid configuration initialization filter Exception", e);
		}
		
		List filterList=new ArrayList<>();
        filterList.add(wallFilter());
        
        datasource.setProxyFilters(filterList);
	}

}
