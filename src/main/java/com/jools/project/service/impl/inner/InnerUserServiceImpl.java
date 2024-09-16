package com.jools.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jools.joolscommon.model.entity.User;
import com.jools.joolscommon.service.InnerUserService;
import com.jools.project.common.ErrorCode;
import com.jools.project.exception.BusinessException;
import com.jools.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/10 23:23
 * @description: TODO
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        //参数校验
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //创建查询包装器
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey", accessKey);

        //使用 UserMapper 的 selectOne 方法查询
        return userMapper.selectOne(userQueryWrapper);
    }
}
