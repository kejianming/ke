package com.nfdw.config;
//package com.nfdw.config;
//
//import javax.annotation.Resources;
//import javax.sql.DataSource;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.TransactionManagementConfigurer;
//
///**
// * 
// * @Description: mybatis配置类
// * @Author Ivan Lee
// * @Date 2019年1月7日
// */
//@Configuration
//@EnableTransactionManagement
//public class MybatisConfig implements TransactionManagementConfigurer {
//	
//	@Autowired
//    private DataSource dataSource;
//	
//	@Bean(name="sqlSessionTemplete")
//    public SqlSessionTemplate initSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
//		return sqlSessionTemplate;
//    }
//	
//	@Override
//	public PlatformTransactionManager annotationDrivenTransactionManager() {
//		return new DataSourceTransactionManager(dataSource);
//	}
//
//}
