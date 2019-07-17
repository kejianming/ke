package com.nfdw.mapper;

import com.github.pagehelper.Page;
import com.nfdw.entity.PageData;

public interface EquipmentMapper {

    Page<PageData> selectDetectionList(PageData pd);
}
