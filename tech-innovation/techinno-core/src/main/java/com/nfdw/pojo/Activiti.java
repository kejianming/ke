package com.nfdw.pojo;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nfdw.entity.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.activiti.engine.form.FormProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
public class Activiti {

    //编号
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    //密级
    private String secret;

    //紧急情况
    private String urgency;

    //流水号
    @Column(name = "serial_number")
    private String serialNumber;

    //填表时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tbsj;

    //承办人编号
    @Column(name = "cbr_Id")
    private String cbrId;

    //附件id
    //@Column(name = "file_id")
    //private String fileId;//附件id

    //附件ids
    @Column(name = "file_Ids")
    private String fileIds;

    //备注
    private String remark;

    //流程id
    @Column(name = "process_Id")
    private String processId;

    //@Column(name = "task_Name")
    //private String taskName;

    //是否删除
    @Column(name = "is_Delete")
    private Integer isDelete;

    //获取附件
    //@Transient
    //private File file;

    //负责人（下一步承办人id）
    @Column(name = "fzr_Id")
    private String fzrId;

    //标题
    private String title;

    //公共流程类id
    @Column(name = "list_Id")
    private String listId;


    /**
     * 获取下一步执行人
     */
    @Transient
    private Map<String, List<SysUser>> nextMap;

    /**
     * 获取回退执行人
     */
    @Transient
    private Map<String, List<SysUser>> returnMap;

    /**
     * 动态表单
     */
    @Transient
    private Map<String, FormProperty> formPropertyMap;

    /**
     * 审批人集合
     */
    @Transient
    private List<SysUser> spList;

    /**
     * 回退节点
     */
    @Transient
    private String returnActiviti;

    @Transient
    private List<SysDepartment> departments;

    /**
     * 传阅人集合
     */
    @Transient
    private String[] userIds;

    /**
     * 类型
     */
    @Transient
    private int type;

    /**
     * 催办时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Transient
    private Date cbTime;

    @Transient
    private int isSend;

    @Transient
    private String t;


    @Transient
    private int processTypeId;

    @Transient
    private boolean isNoNode;

}
