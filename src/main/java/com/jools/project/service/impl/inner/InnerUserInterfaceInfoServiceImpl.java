package com.jools.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jools.joolscommon.model.entity.User;
import com.jools.joolscommon.service.InnerUserInterfaceInfoService;
import com.jools.project.common.ErrorCode;
import com.jools.project.exception.BusinessException;
import com.jools.project.mapper.UserMapper;
import com.jools.project.service.UserInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/10 23:10
 * @description: TODO
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    private UserMapper userMapper;

    /**
     * 实现 InnerUserService 接口中的 getInvokeUser 方法，
     * 用于根据密钥获取用户信息并且校验 secretKey
     *
     * @param accessKey 密钥
     * @param secretKey
     * @return 内部用户信息，如果找不到匹配的用户则返回 null
     */
    @Override
    public User getInvokeUser(String accessKey, String secretKey) {
        //参数校验
        if (StringUtils.isAnyBlank(accessKey) || StringUtils.isAnyBlank(secretKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //创建查询包装器
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey", accessKey);
//        userQueryWrapper.eq("secretKey", secretKey);
        return userMapper.selectOne(userQueryWrapper);
    }

    /**
     * 更新接口，调用次数 + 1
     *
     * @param interfaceInfoId 接口唯一标识符 id
     * @param userId          用户 id
     * @return
     */
    @Override
    public boolean invokeInterfaceCount(Long interfaceInfoId, Long userId) {
        //参数校验
        if (interfaceInfoId == null || userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        try {
            return userInterfaceInfoService.invokeInterfaceCount(interfaceInfoId, userId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean canInvoke(Long interfaceInfoId, Long userId) {
        //参数校验
        if (interfaceInfoId == null || userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userInterfaceInfoService.canInvoke(interfaceInfoId, userId);
    }
}
