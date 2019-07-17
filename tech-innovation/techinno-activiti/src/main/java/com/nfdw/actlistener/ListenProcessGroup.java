package com.nfdw.actlistener;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nfdw.entity.SysProcessGroup;
import com.nfdw.util.JsonUtil;

//@Aspect
//@Component
public class ListenProcessGroup {
	@Autowired
    IdentityService identityService;
	 //同步流程组
    @Around("execution(com.nfdw.util.JsonUtil com.nfdw.controller.ProcessGroupController.addProcessGroup(..))")
    public Object listenProcessGroupInsert(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = joinPoint.proceed(joinPoint.getArgs());
        JsonUtil j = (JsonUtil) o;
        if (j.isFlag()) {
            Object[] args = joinPoint.getArgs();
            if (args.length == 2) {
                changeProcessGroup(args);
            }
        }
        return o;
    }
    @Around("execution(com.nfdw.util.JsonUtil com.nfdw.controller.ProcessGroupController.updateUser(..))")
    public Object listenProcessGroupUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = joinPoint.proceed(joinPoint.getArgs());
        Object[] args = joinPoint.getArgs();
        if (args.length == 1) {
            if (((JsonUtil) o).isFlag()) {
                updateProcessGroup(args);
            }
        }

        return o;
    }
    @Around("execution(com.nfdw.util.JsonUtil com.nfdw.controller.ProcessGroupController.del(..))")
    public Object listenDelProcessGroup(ProceedingJoinPoint point) throws Throwable {
        Object o = point.proceed(point.getArgs());
        JsonUtil util = (JsonUtil) o;
        if (util.isFlag()) {
            Object[] args = point.getArgs();
            identityService.deleteGroup(args[0].toString());
        }
        return o;
    }
    public void changeProcessGroup(Object[] obj) {
        SysProcessGroup processGroup = (SysProcessGroup) obj[0];
        Group group = new GroupEntity();
        group.setId(processGroup.getId().toString());
        group.setName(processGroup.getName());
        identityService.saveGroup(group);
    }
    public void updateProcessGroup(Object[] obj){
    	SysProcessGroup processGroup = (SysProcessGroup) obj[0];
        identityService.deleteGroup(processGroup.getId().toString());
        Group group = new GroupEntity();
        group.setId(processGroup.getId().toString());
        group.setName(processGroup.getName());
        identityService.saveGroup(group);
    }

}
