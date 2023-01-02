package cn.liyu.log;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 */
@EnableAsync
@Configuration
@ComponentScan("cn.liyu.log")
@MapperScan(basePackages = "cn.liyu.log.mapper")
public class LogAutoConfiguration {


}

