package com.nfdw.mapper;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;
import ${tableClass.fullClassName};;

public interface ${tableClass.shortClassName}Mapper  extends tk.mybatis.mapper.common.Mapper<${tableClass.shortClassName}>,MySqlMapper<${tableClass.shortClassName}>, IdsMapper<${tableClass.shortClassName}> {
}