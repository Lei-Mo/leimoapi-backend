package service;

import com.baomidou.mybatisplus.extension.service.IService;
import model.entity.InterfaceInfo;
import model.entity.User;
import model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    /**
     * 调用次数统计
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 数据库中查是否已分配用户密钥
     * // todo 还需要查询用户是否还有调用次数
     */
    User getInvokeUser(String accessKey, String secretKey);

    /**
     * 从数据库中查询接口是否存在（请求路径，请求方法，请求参数）
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}