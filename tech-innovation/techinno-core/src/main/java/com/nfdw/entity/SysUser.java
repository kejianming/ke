package com.nfdw.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.nfdw.validator.group.AddGroup;
import com.nfdw.validator.group.UpdateGroup;

import java.util.Date;

@Table(name = "sys_user")
@Data
@ToString
public class SysUser {
    @Id
    @GeneratedValue(generator = "JDBC")
    private String id;

    @NotEmpty(message = "用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String username;

    @NotEmpty(message = "密码不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String password;

    private Integer age;

    private String email;

    private String photo;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 0可用1封禁
     */
    @Column(name = "del_flag")
    private Byte delFlag;

    @Column(name = "department_id")
    private Integer departmentId;
    
    @Transient
    private String gzarea;

}