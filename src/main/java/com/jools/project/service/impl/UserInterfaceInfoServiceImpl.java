package com.jools.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jools.project.common.ErrorCode;
import com.jools.project.mapper.UserInterfaceInfoMapper;
import com.jools.project.model.entity.UserInterfaceInfo;
import com.jools.project.service.UserInterfaceInfoService;
import com.jools.project.exception.BusinessException;
import com.jools.project.model.vo.UserInterfaceInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author 10355
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2024-08-30 20:23:33
 * <p>
 * create table if not exists `user_interface_info` (
 * `id` bigint not null auto_increment comment '主键' primary key,
 * `userId` bigint not null comment '调用用户 id',
 * `interfaceInfoId` bigint not null comment '接口 id',
 * `totalNum` int default 0 not null comment '总调用次数',
 * `leftNum` int default 0 not null comment '剩余调用次数',
 * `status` int default 0 not null comment '0-正常，1-禁用',
 * `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
 * `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
 * `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
 * ) comment '用户调用接口关系';
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, Boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //id 不能为空
        Long id = userInterfaceInfo.getId();
        if (id == null || id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口Id不能为空!");
        }

        //调用用户的 Id 不能为空
        Long userId = userInterfaceInfo.getUserId();
        if (userId == null || userId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户Id不能为空!");
        }

        //总共可调用次数不少于 0
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();

        if (leftNum == null || leftNum < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余可调用次数不少于 0");
        }

        Integer isDelete = userInterfaceInfo.getIsDelete();
        if (null == isDelete || (1 != isDelete && 0 != isDelete)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更新删除状态不正确");
        }

        // 创建时，参数不能为空
        if (add) {
            if (StringUtils.isAnyBlank(totalNum.toString())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "总共可调用次数不能为空");
            }
        }
    }

    @Override
    public boolean invokeInterfaceCount(Long interfaceInfoId, Long userId) {

        //检查输入
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数校验失败!");
        }

        //查询数据库更新，基于当前调用的接口 id 和分配的 userId
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);

        //剩余可以调用次数需要大于等于 0
        updateWrapper.gt("leftNum", 0);

        //更新接口剩余的可调用次数和总计调用次数
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");

        return this.update(updateWrapper);
    }

    @Override
    public UserInterfaceInfoVO convert2VO(UserInterfaceInfo userInterfaceInfo) {
        UserInterfaceInfoVO VO = new UserInterfaceInfoVO();
        BeanUtils.copyProperties(userInterfaceInfo, VO);
        return VO;
    }
}




