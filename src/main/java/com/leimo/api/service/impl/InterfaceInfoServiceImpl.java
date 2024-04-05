package com.leimo.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leimo.api.common.ErrorCode;
import com.leimo.api.exception.BusinessException;
import com.leimo.api.mapper.InterfaceInfoMapper;
import com.leimo.api.model.entity.InterfaceInfo;
import com.leimo.api.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 86178
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-02-29 20:59:22
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {


        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestParams = interfaceInfo.getRequestParams();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        Integer status = interfaceInfo.getStatus();
        String method = interfaceInfo.getMethod();
        Long userId = interfaceInfo.getUserId();
        Date createTime = interfaceInfo.getCreateTime();
        Date updateTime = interfaceInfo.getUpdateTime();
        Integer isDelete = interfaceInfo.getIsDelete();
        // 创建时，所有参数必须不能为空
        if (add) {
            if (StringUtils.isAnyBlank(name, url, requestParams, method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        // 如果接口名称不为空且长度大于50,抛出参数错误的异常,错误信息为"名称过长"
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
        // if (StringUtils.isNotBlank(content) && content.length() > 8192) {
        //     throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        // }
        // if (reviewStatus != null && !InterfaceInfoReviewStatusEnum.getValues().contains(reviewStatus)) {
        //     throw new BusinessException(ErrorCode.PARAMS_ERROR);
        // }
        // if (gender != null && !InterfaceInfoGenderEnum.getValues().contains(gender)) {
        //     throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别不符合要求");
        // }
    }
}




