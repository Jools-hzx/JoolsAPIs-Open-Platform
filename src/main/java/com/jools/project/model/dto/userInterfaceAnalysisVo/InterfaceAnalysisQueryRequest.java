package com.jools.project.model.dto.userInterfaceAnalysisVo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询用户-接口关系 请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode()
@Data
public class InterfaceAnalysisQueryRequest implements Serializable {

    /**
     * 返回前 Limit 的记录
     */
    private Integer limit;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}