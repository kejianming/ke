package com.nfdw.entity;

import org.activiti.engine.runtime.ProcessInstance;

public class ProcessInstanceInfo {

    private String processDefinitionId;
    private String processDefinitionName;
    private String deploymentId;
    private String name;
    private String processInstanceId;

    public ProcessInstanceInfo(ProcessInstance processInstance) {
        this.processDefinitionId = processInstance.getProcessDefinitionId();
        this.processDefinitionName = processInstance.getProcessDefinitionName();
        this.deploymentId = processInstance.getDeploymentId();
        this.processInstanceId = processInstance.getProcessInstanceId();
        this.name = processInstance.getName();
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
