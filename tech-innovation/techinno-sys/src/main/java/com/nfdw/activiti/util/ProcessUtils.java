package com.nfdw.activiti.util;

import lombok.Getter;
import lombok.ToString;
import org.activiti.engine.*;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nfdw.entity.*;
import com.nfdw.mapper.*;
import com.nfdw.pojo.Activiti;
import com.nfdw.util.BeanUtil;

import java.util.*;


@Getter
@ToString
@Component
/**
 * 流程工具类，该类所以方法只适用与单行节点流程
 */
public class ProcessUtils {


    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    FormService formService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    HistoryService historyService;

    @Autowired
    SysDepartmentMapper sysDepartmentMapper;

    @Autowired
    SysRoleUserMapper sysRoleUserMapper;

    private ProcessInstance processInstance;

    private Task previousTask;

    private Task nowTask;

    private Task nextTask;

    private boolean isNoNode;

    //开启流程
    public void init(String key, String cbrId) {
        this.processInstance = runtimeService.startProcessInstanceByKey(key, cbrId);
        this.nowTask = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        taskService.setAssignee(nowTask.getId(), cbrId);
    }

    public void init(String processInstanceId) {
        this.processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        this.nowTask = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
    }
    
    public void close() {
        this.taskService.complete(this.nowTask.getId());
    }
    /**
     * 转下一步
     *
     * @param fzrId 负责人ID
     */
    public void nextProcess(String fzrId) {
        this.taskService.complete(this.nowTask.getId());
        this.previousTask = this.nowTask;
        this.nowTask = this.taskService.createTaskQuery().processInstanceId(this.processInstance.getProcessInstanceId()).singleResult();
        this.nextTask = this.nowTask;
        this.taskService.setAssignee(this.nextTask.getId(), fzrId);
    }
    
    private void previousStep(List<PvmTransition> pvmTransitions, Map<String, List<SysUser>> returnMap, String assignee, Integer departmentId, boolean is) {
        if (null == pvmTransitions) {
            ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(this.nowTask.getExecutionId())
                    .singleResult();

            String activitiId = execution.getActivityId();

            ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(nowTask.getProcessDefinitionId());
            ActivityImpl activityImpl = def.findActivity(activitiId);
            previousStep(activityImpl.getIncomingTransitions(), returnMap, assignee, departmentId, is);
        } else {
            for (PvmTransition pvmTransition : pvmTransitions) {
                /**
                 * 下一步为空，表示已经到达第一步
                 */
                //if (is) {
                if (pvmTransition.getSource().getIncomingTransitions().size() > 0) {
                    for (PvmTransition pt : pvmTransition.getSource().getIncomingTransitions()) {
                        PvmActivity ac = pt.getDestination();
                        TaskDefinition taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();

                        /**
                         * 判读前一步是否为空，为空就是第二步
                         */
                        if (pt.getSource().getIncomingTransitions().size() == 0) {
                            if (null != taskDefinition) {
                                List<SysUser> users = new ArrayList<>();
                                users.add(sysUserMapper.selectByPrimaryKey(assignee));
                                returnMap.put(taskDefinition.getKey() + "-" + taskDefinition.getNameExpression().getExpressionText()
                                        , users);
                            }
                        } else {
                            if (null != taskDefinition) {
                                Iterator<Expression> iterator = taskDefinition.getCandidateGroupIdExpressions().iterator();
                                while (iterator.hasNext()) {
                                    String roleId = iterator.next().getExpressionText();
                                    List<SysUser> users = sysUserMapper.queryUserByRoleIdAndDepartmentId(roleId, getDepartmentId(roleId, departmentId));
                                    returnMap.put(taskDefinition.getKey() + "-" + taskDefinition.getNameExpression().getExpressionText(), users);
                                }
                            }
                        }
                    }
                    if (is) {
                        previousStep(pvmTransition.getSource().getIncomingTransitions(), returnMap, assignee, departmentId, is);
                    }
                }
            }
        }
    }
    /**
     * 获取当前任务表单
     *
     * @param activiti
     * @param departmentId
     */
    public Map<String, FormProperty> getTaskFormData(Activiti activiti, Integer departmentId) {
        List<FormProperty> formProperties = formService.getTaskFormData(nowTask.getId()).getFormProperties();
        Map<String, FormProperty> formPropertyMap = null;
        if (null != formProperties && formProperties.size() > 0) {
            formPropertyMap = new LinkedHashMap<>();
            for (FormProperty formProperty : formProperties
            ) {
                formPropertyMap.put(formProperty.getId(), formProperty);
            }
            if (formPropertyMap.size() > 0) {
                if (null != formPropertyMap.get("sp")) {
                    List<IdentityLink> identityLinksForTask = this.getTaskService().getIdentityLinksForTask(this.getNowTask().getId());
                    String roleId = identityLinksForTask.get(0).getGroupId();
                    activiti.setSpList(sysUserMapper.queryUserByRoleIdAndDepartmentId(roleId, getDepartmentId(roleId, sysUserMapper.selectByPrimaryKey(this.getNowTask().getAssignee()).getDepartmentId())));
                }

                if (null != formPropertyMap.get("next")) {
                    Map<String, List<SysUser>> nextMap = new LinkedHashMap<>();
                    this.nextNode(nextMap, departmentId);
                    activiti.setNextMap(nextMap);
                }
                if (null != formPropertyMap.get("return")) {
                    List<PvmTransition> pvmTransitions = null;
                    Map<String, List<SysUser>> returnMap = new LinkedHashMap<>();
                    this.previousStep(pvmTransitions, returnMap, activiti.getCbrId(), departmentId, formPropertyMap.get("is") == null);
                    activiti.setReturnMap(returnMap);
                }

            }
        }
        return formPropertyMap;
    }
    private void nextNode(Map<String, List<SysUser>> nextMap, Integer departmentId) {
        ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(this.nowTask.getExecutionId())
                .singleResult();

        String activitiId = execution.getActivityId();

        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(this.nowTask.getProcessDefinitionId());
        ActivityImpl activityImpl = def.findActivity(activitiId);

        List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
        for (PvmTransition tr : outTransitions) {
            PvmActivity ac = tr.getDestination();
            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();

            if (null != taskDefinition) {
                Iterator<Expression> iterator = taskDefinition.getCandidateGroupIdExpressions().iterator();

                List<SysUser> users = new ArrayList<>();

                while (iterator.hasNext()) {
                    String roleId = iterator.next().getExpressionText();
                    users = sysUserMapper.queryUserByRoleIdAndDepartmentId(roleId, getDepartmentId(roleId, departmentId));
                }
                nextMap.put(taskDefinition.getKey() + "-" + taskDefinition.getNameExpression().getExpressionText(), users);
            }
        }
    }
    public void Withdraw(String upperId, Activiti activiti) {
        ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(nowTask.getExecutionId())
                .singleResult();

        String activitiId = execution.getActivityId();
        //获取流程定义的所有节点
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(nowTask.getProcessDefinitionId());
        ActivityImpl currActivity = def.findActivity(activitiId);

        //获取上一部活动出口
        String nextActiviti;

        if (null != activiti.getReturnActiviti()) {
            nextActiviti = activiti.getReturnActiviti();
        } else {
        	//退回到上节点
            nextActiviti = currActivity.getIncomingTransitions().get(0).getSource().getId();
        }

        List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
        // 备份原始活动出口
        List<PvmTransition> oriPvmTransitionList = new ArrayList<>();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        try {
            // 清除当前活动的出口
            pvmTransitionList.clear();

            // 建立新出口
            TransitionImpl newTransition = currActivity
                    .createOutgoingTransition();
            ActivityImpl nextActivityImpl = def.findActivity(nextActiviti);

            newTransition.setDestination(nextActivityImpl);

            // 判断是否是 撤回 或者 回退
            if (null != activiti.getFzrId()) {
                nextProcess(activiti.getFzrId()); //回退
            } else {
                nextProcess(upperId);
            }
            //清楚历史数据
            historyService.deleteHistoricTaskInstance(previousTask.getId());

            // 删除目标节点新流入
            nextActivityImpl.getIncomingTransitions().remove(newTransition);

            //清空当前活动
            currActivity.getOutgoingTransitions().clear();


        } catch (Exception ex) {
            throw ex;

        } finally {
            //还原备份活动
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                currActivity.getOutgoingTransitions().add(pvmTransition);
            }
        }
    }
    /**
     * 全体人员默认部门
     * @param roleId
     * @param departmentId
     * @return
     */
    private Integer getDepartmentId(String roleId, Integer departmentId) {
        return roleId.equals("dcb0f642fe9611e7b472201a068c6482") || roleId.equals("023366f3457511e8bcf1309c2315f9aa") ? departmentId : null;
    }
}
