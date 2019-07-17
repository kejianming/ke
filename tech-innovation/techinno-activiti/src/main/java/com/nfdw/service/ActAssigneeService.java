package com.nfdw.service;

import com.nfdw.base.service.BaseService;
import com.nfdw.entity.ActAssignee;

import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.List;

/**

 */
public interface ActAssigneeService extends BaseService<ActAssignee,String> {
  int deleteByNodeId(String nodeId);

  public List<ActivityImpl> getActivityList(String deploymentId);

  public  List<ActivityImpl> selectAllActivity(List<ActivityImpl> activities);

}
