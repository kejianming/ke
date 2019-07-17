package com.nfdw.actlistener;

import com.nfdw.entity.ActAssignee;
import com.nfdw.service.ActAssigneeService;
import com.nfdw.service.impl.ActAssigneeServiceImpl;
import com.nfdw.util.AssigneeType;
import com.nfdw.util.SpringUtil;

import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**

 * 流程监听器 动态注入节点办理人
 */
public class ActNodeListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        //KEY
        String nodeId = delegateTask.getTaskDefinitionKey();
        ActAssigneeService actAssigneeService = SpringUtil.getBean(ActAssigneeServiceImpl.class);
        List<ActAssignee> assigneeList = actAssigneeService.selectListByPage(new ActAssignee(nodeId));
        for (ActAssignee assignee : assigneeList) {
            switch (assignee.getAssigneeType()) {
                case AssigneeType.GROUP_TYPE:
                    delegateTask.addCandidateGroup(assignee.getRoleId());
                    break;
                case AssigneeType.USER_TYPE:
                    delegateTask.addCandidateUser(assignee.getAssignee());
                    break;
            }
        }
    }
}
