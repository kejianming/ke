package com.nfdw.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "sys_department")
@Data
@ToString
@EqualsAndHashCode
public class SysDepartment {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "creater")
    private String creater;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "gzarea")
    private String gzarea;
}
