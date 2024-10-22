package com.leimo.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leimo.api.model.entity.InterfaceInfo;

/**
 * @author 86178
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2024-02-29 20:59:22
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
}
