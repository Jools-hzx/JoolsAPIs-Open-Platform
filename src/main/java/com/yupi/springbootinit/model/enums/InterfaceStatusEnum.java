package com.yupi.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/24 23:15
 * @description: 管理接口状态的枚举类
 */
public enum InterfaceStatusEnum {

    ONLINE(1),

    OFFLINE(0);

    private final Integer value;

    InterfaceStatusEnum(Integer val) {
        this.value = val;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 查找的状态值 value
     */
    public static InterfaceStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (InterfaceStatusEnum anEnum : InterfaceStatusEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }
}
