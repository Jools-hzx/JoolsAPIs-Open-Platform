package com.jools.project.model.dto.interfacesInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 调用远程接口请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfacesInfoInvokeRequest implements Serializable {

    /**
     * 接口Id(主键)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}