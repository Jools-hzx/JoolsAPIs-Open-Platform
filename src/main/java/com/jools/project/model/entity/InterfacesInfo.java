package com.jools.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息表
 *
 * @TableName interfaces_info
 */
@TableName(value = "interfaces_info")
@Data
public class InterfacesInfo implements Serializable {
    /**
     * 用户Id(主键)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口url
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 请求参数
     * [
     *  {"name": "username", "type": "string"}
     * ]
     */
    private String requestParams;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态( 0 - 关闭，1 - 开启)
     */
    private Integer status;

    /**
     * 请求类型（GET/POST）
     */
    private String method;

    /**
     * 创建人 Id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除 (0 - 未被删除，1 - 已经删除)
     */
    @TableLogic  //逻辑删除添加注解
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}