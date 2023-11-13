package org.pet.home.service;

import org.pet.home.entity.UserMsg;
import org.apache.ibatis.annotations.Param;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public interface IUserMsgService {
    int add(UserMsg userMsg);

    int addTask(@Param("shopId") long shopId, @Param("employeeId")long employeeId,
                @Param("petId")long petId, @Param("userId")long userId, @Param("userMsgId")long userMsgId);


}
