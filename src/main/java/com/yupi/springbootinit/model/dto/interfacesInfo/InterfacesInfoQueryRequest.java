package com.yupi.springbootinit.model.dto.interfacesInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfacesInfoQueryRequest extends PageRequest implements Serializable {

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

    //删除! 用户不太可能根据创建时间、更新时间、是否删除查询，一般都是范围查询

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}