package cn.liyu.log.aspect;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.liyu.log.constant.LogTypeEnum;
import cn.liyu.log.entity.SysLog;
import cn.liyu.log.event.SysLogEvent;
import cn.liyu.log.utils.IPUtil;
import cn.liyu.log.utils.LogUtil;
import cn.liyu.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * ①切面注解得到请求数据 -> ②发布监听事件 -> ③异步监听日志入库
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    private ThreadLocal<SysLog> sysLogThreadLocal = new ThreadLocal<>();

    /**
     * 事件发布是由ApplicationContext对象管控的，我们发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
     **/
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(cn.liyu.log.annotation.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /***
     * 拦截控制层的操作日志
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Before(value = "logPointcut()")
    public void recordLog(JoinPoint joinPoint) throws Throwable {
        SysLog sysLog = new SysLog();
        //将当前实体保存到threadLocal
        sysLogThreadLocal.set(sysLog);
        // 开始时间
        long beginTime = Instant.now().toEpochMilli();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String usernameOrAnonymous = SecurityUtils.getCurrentUsernameOrAnonymous();
        sysLog.setUserName(usernameOrAnonymous);
        sysLog.setActionUrl(URLUtil.getPath(request.getRequestURI()));
        sysLog.setStartTime(LocalDateTime.now());
        String ip = ServletUtil.getClientIP(request);
        sysLog.setIp(ip);
        sysLog.setLocation(IPUtil.getCityInfo(ip));
        sysLog.setRequestMethod(request.getMethod());
        String uaStr = request.getHeader("user-agent");
        sysLog.setBrowser(UserAgentUtil.parse(uaStr).getBrowser().toString());
        sysLog.setOs(UserAgentUtil.parse(uaStr).getOs().toString());

        //访问目标方法的参数 可动态改变参数值
        Object[] args = joinPoint.getArgs();
        //获取执行的方法名
        sysLog.setActionMethod(joinPoint.getSignature().getName());
        // 类名
        sysLog.setClassPath(joinPoint.getTarget().getClass().getName());
        sysLog.setActionMethod(joinPoint.getSignature().getName());
        sysLog.setFinishTime(LocalDateTime.now());
        // 参数
        sysLog.setParams(Arrays.toString(args));
        sysLog.setDescription(LogUtil.getControllerMethodDescription(joinPoint));
        long endTime = Instant.now().toEpochMilli();
        sysLog.setConsumingTime(endTime - beginTime);
    }

    /**
     * 返回通知
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "logPointcut()")
    public void doAfterReturning(Object ret) {
        //得到当前线程的log对象
        SysLog sysLog = sysLogThreadLocal.get();
        sysLog.setType(LogTypeEnum.NORMAL.getType());
        // 处理完请求，返回内容
        if (ret == null) {
            return;
        }
        try {
            String s = JSON.toJSONString(ret);
            sysLog.setExDetail(s);
            // 发布事件
            applicationContext.publishEvent(new SysLogEvent(sysLog));
        } catch (Exception e) {
        } finally {
            //移除当前log实体
            sysLogThreadLocal.remove();
        }
    }

    /**
     * 异常通知
     *
     * @param e
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        SysLog sysLog = sysLogThreadLocal.get();
        // 异常
        sysLog.setType(LogTypeEnum.EXCEPTION.getType());
        // 异常对象
        sysLog.setExDetail(LogUtil.getStackTrace(e));
        // 异常信息
        sysLog.setExDesc(e.getMessage());
        // 发布事件
        applicationContext.publishEvent(new SysLogEvent(sysLog));
        //移除当前log实体
        sysLogThreadLocal.remove();
    }
}
