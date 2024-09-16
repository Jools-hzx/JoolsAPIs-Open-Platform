package com.jools.project.model.vo;

import com.jools.joolscommon.model.entity.InterfacesInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/16 15:44
 * @description: 接口调用次数统计 VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceAnalysisVO extends InterfacesInfo {

    //统计调用次数
    private Integer totalNum;
}
