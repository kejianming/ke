package com.nfdw.service.impl;

import com.nfdw.base.BaseMapper;
import com.nfdw.base.service.impl.BaseServiceImpl;
import com.nfdw.entity.UserLeave;
import com.nfdw.mapper.UserLeaveMapper;
import com.nfdw.service.UserLeaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**

 */
@Service
public class UserLeaveServiceImpl extends BaseServiceImpl<UserLeave,String> implements
    UserLeaveService {

  @Autowired
  UserLeaveMapper userLeaveMapper;

  @Override
  public BaseMapper<UserLeave,String> getMappser() {
    return userLeaveMapper;
  }
}
