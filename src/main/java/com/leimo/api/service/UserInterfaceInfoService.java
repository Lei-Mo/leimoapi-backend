package com.leimo.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leimo.api.model.entity.UserInterfaceInfo;

/**
 * @author 86178
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    /**
     * 调用次数统计
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
