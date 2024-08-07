package com.yupi.springbootinit.model.dto.interfacesInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfacesInfoUpdateRequest implements Serializable {

    /**
     * 用户Id(主键)
     * 主键 id 不能够被更改，但是我们更新对象需要知道是更新了哪一条数据
     */
//    @TableId(type = IdType.AUTO)
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

    /*
     创建人一般不会随意修改，但是可以记录管理员创建者
     */

    /**
     * 创建人 Id
     */
//    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}