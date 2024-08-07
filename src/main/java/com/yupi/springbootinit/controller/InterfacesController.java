package com.yupi.springbootinit.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.annotation.AuthCheck;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.DeleteRequest;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.constant.UserConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.interfacesInfo.InterfacesInfoAddRequest;
import com.yupi.springbootinit.model.dto.interfacesInfo.InterfacesInfoQueryRequest;
import com.yupi.springbootinit.model.dto.interfacesInfo.InterfacesInfoUpdateRequest;
import com.yupi.springbootinit.model.entity.InterfacesInfo;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.vo.InterfacesInfoVO;
import com.yupi.springbootinit.service.InterfacesInfoService;
import com.yupi.springbootinit.service.UserService;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/interfacesInfo")
@Slf4j
public class InterfacesController {

    @Resource
    private InterfacesInfoService interfacesInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfacesInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfacesInfo(@RequestBody InterfacesInfoAddRequest interfacesInfoAddRequest, HttpServletRequest request) {
        if (interfacesInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfacesInfo interfacesInfo = new InterfacesInfo();
        BeanUtils.copyProperties(interfacesInfoAddRequest, interfacesInfo);

        //校验
        interfacesInfoService.validInterfacesInfo(interfacesInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfacesInfo.setUserId(loginUser.getId());

        boolean result = interfacesInfoService.save(interfacesInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfacesInfoId = interfacesInfo.getId();
        return ResultUtils.success(newInterfacesInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfacesInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfacesInfo oldInterfacesInfo = interfacesInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfacesInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfacesInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfacesInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfacesInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfacesInfo(@RequestBody InterfacesInfoUpdateRequest interfacesInfoUpdateRequest) {
        if (interfacesInfoUpdateRequest == null || interfacesInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfacesInfo interfacesInfo = new InterfacesInfo();
        BeanUtils.copyProperties(interfacesInfoUpdateRequest, interfacesInfo);

        // 参数校验
        interfacesInfoService.validInterfacesInfo(interfacesInfo, false);
        long id = interfacesInfoUpdateRequest.getId();
        // 判断是否存在
        InterfacesInfo oldInterfacesInfo = interfacesInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfacesInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfacesInfoService.updateById(interfacesInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfacesInfoVO> getInterfacesInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfacesInfo interfacesInfo = interfacesInfoService.getById(id);
        if (interfacesInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
//        return ResultUtils.success(interfacesInfoService.getInterfacesInfoVO(interfacesInfo, request));
        return null;
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param interfacesInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfacesInfo>> listInterfacesInfoByPage(@RequestBody InterfacesInfoQueryRequest interfacesInfoQueryRequest) {
        long current = interfacesInfoQueryRequest.getCurrent();
        long size = interfacesInfoQueryRequest.getPageSize();

        String sortField = interfacesInfoQueryRequest.getSortField();
        String sortOrder = interfacesInfoQueryRequest.getSortOrder();

        InterfacesInfo interfacesInfoQuery = new InterfacesInfo();
        String description = interfacesInfoQuery.getDescription();

        // description 需要支持模糊搜索
        interfacesInfoQuery.setDescription(null);

        //限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfacesInfo> queryWrapper = new QueryWrapper<>(interfacesInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(
                StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.page(new Page<>(current, size));
//        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.page(new Page<>(current, size),
//                interfacesInfoService.getQueryWrapper(interfacesInfoQueryRequest));
        return ResultUtils.success(interfacesInfoPage);
    }

//    /**
//     * 分页获取列表（封装类）
//     *
//     * @param interfacesInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/list/page/vo")
//    public BaseResponse<Page<InterfacesInfoVO>> listInterfacesInfoVOByPage(@RequestBody InterfacesInfoQueryRequest interfacesInfoQueryRequest,
//                                                                           HttpServletRequest request) {
//        long current = interfacesInfoQueryRequest.getCurrent();
//        long size = interfacesInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.page(new Page<>(current, size),
//                interfacesInfoService.getQueryWrapper(interfacesInfoQueryRequest));
//        return ResultUtils.success(interfacesInfoService.getInterfacesInfoVOPage(interfacesInfoPage, request));
//    }
//
//    /**
//     * 分页获取当前用户创建的资源列表
//     *
//     * @param interfacesInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/my/list/page/vo")
//    public BaseResponse<Page<InterfacesInfoVO>> listMyInterfacesInfoVOByPage(@RequestBody InterfacesInfoQueryRequest interfacesInfoQueryRequest,
//                                                                             HttpServletRequest request) {
//        if (interfacesInfoQueryRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User loginUser = userService.getLoginUser(request);
//        interfacesInfoQueryRequest.setUserId(loginUser.getId());
//        long current = interfacesInfoQueryRequest.getCurrent();
//        long size = interfacesInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.page(new Page<>(current, size),
//                interfacesInfoService.getQueryWrapper(interfacesInfoQueryRequest));
//        return ResultUtils.success(interfacesInfoService.getInterfacesInfoVOPage(interfacesInfoPage, request));
//    }
//
//    // endregion
//
//    /**
//     * 分页搜索（从 ES 查询，封装类）
//     *
//     * @param interfacesInfoQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/search/page/vo")
//    public BaseResponse<Page<InterfacesInfoVO>> searchInterfacesInfoVOByPage(@RequestBody InterfacesInfoQueryRequest interfacesInfoQueryRequest,
//                                                                             HttpServletRequest request) {
//        long size = interfacesInfoQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<InterfacesInfo> interfacesInfoPage = interfacesInfoService.searchFromEs(interfacesInfoQueryRequest);
//        return ResultUtils.success(interfacesInfoService.getInterfacesInfoVOPage(interfacesInfoPage, request));
//    }

//    /**
//     * 编辑（用户）
//     *
//     * @param interfacesInfoEditRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/edit")
//    public BaseResponse<Boolean> editInterfacesInfo(@RequestBody InterfacesInfoEditRequest interfacesInfoEditRequest, HttpServletRequest request) {
//        if (interfacesInfoEditRequest == null || interfacesInfoEditRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        InterfacesInfo interfacesInfo = new InterfacesInfo();
//        BeanUtils.copyProperties(interfacesInfoEditRequest, interfacesInfo);
//        List<String> tags = interfacesInfoEditRequest.getTags();
//        if (tags != null) {
//            interfacesInfo.setTags(JSONUtil.toJsonStr(tags));
//        }
//        // 参数校验
//        interfacesInfoService.validInterfacesInfo(interfacesInfo, false);
//        User loginUser = userService.getLoginUser(request);
//        long id = interfacesInfoEditRequest.getId();
//        // 判断是否存在
//        InterfacesInfo oldInterfacesInfo = interfacesInfoService.getById(id);
//        ThrowUtils.throwIf(oldInterfacesInfo == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可编辑
//        if (!oldInterfacesInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        boolean result = interfacesInfoService.updateById(interfacesInfo);
//        return ResultUtils.success(result);
//    }

}
