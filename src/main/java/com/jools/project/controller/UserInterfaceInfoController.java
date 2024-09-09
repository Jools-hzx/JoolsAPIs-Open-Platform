package com.jools.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jools.joolsclientsdk.client.JoolsHttpClient;
import com.jools.project.common.BaseResponse;
import com.jools.project.common.DeleteRequest;
import com.jools.project.common.ErrorCode;
import com.jools.project.common.ResultUtils;
import com.jools.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.jools.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.jools.project.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.jools.project.model.entity.User;
import com.jools.project.model.entity.UserInterfaceInfo;
import com.jools.project.service.UserInterfaceInfoService;
import com.jools.project.annotation.AuthCheck;
import com.jools.project.constant.CommonConstant;
import com.jools.project.constant.UserConstant;
import com.jools.project.exception.BusinessException;
import com.jools.project.exception.ThrowUtils;

import com.jools.project.model.vo.UserInterfaceInfoVO;
import com.jools.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Jools He
 * @version 1.0
 */

@Slf4j
@Controller
@RequestMapping("/userInterfaceInfo")
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private JoolsHttpClient joolsHttpClient;

    // region 增删改查

    /**
     * 添加用户 - 接口之间的关系
     *
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);

        //校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());

        boolean result = userInterfaceInfoService.save(userInterfaceInfo);

        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newUserInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除接口 - 用户之间的关系
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param userInterfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfacesInfoUpdateRequest) {
        if (userInterfacesInfoUpdateRequest == null || userInterfacesInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfacesInfoUpdateRequest, userInterfaceInfo);

        // 参数校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        long id = userInterfacesInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @ResponseBody
    public BaseResponse<UserInterfaceInfoVO> getUserInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //将 UserInterfaceInfo 转换为 UserInterfaceInfoVo
        UserInterfaceInfoVO vo = userInterfaceInfoService.convert2VO(userInterfaceInfo);

        //转换为 Json 格式字符串
        return ResultUtils.success(vo);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();

        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();

        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
//        String description = userInterfaceInfoQuery.getDescription();

        // description 需要支持模糊搜索
//        userInterfaceInfoQuery.setDescription(null);

        //限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
//        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(
                StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size));
//        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size),
//                userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest));
        return ResultUtils.success(userInterfaceInfoPage);
    }
}
