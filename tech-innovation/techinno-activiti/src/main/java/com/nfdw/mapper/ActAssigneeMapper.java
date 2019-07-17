package com.nfdw.mapper;

import com.nfdw.base.BaseMapper;
import com.nfdw.entity.ActAssignee;

import tk.mybatis.mapper.common.Mapper;

public interface ActAssigneeMapper extends BaseMapper<ActAssignee,String> {
    int deleteByNodeId(String nodeId);
}