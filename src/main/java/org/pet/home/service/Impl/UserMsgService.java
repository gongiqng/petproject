package org.pet.home.service.Impl;

import org.pet.home.dao.UserMsgMapper;
import org.pet.home.entity.UserMsg;
import org.pet.home.service.IUserMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Service
public class UserMsgService implements IUserMsgService {
    private UserMsgMapper userMsgMapper;

    @Autowired
    public UserMsgService(UserMsgMapper userMsgMapper){
        this.userMsgMapper = userMsgMapper;
    }
    @Override
    public int add(UserMsg userMsg) {
        return userMsgMapper.add(userMsg);
    }

    @Override
    public int addTask(long shopId, long employeeId, long petId, long userId, long userMsgId) {
        return userMsgMapper.addTask(shopId, employeeId, petId, userId, userMsgId);
    }
}
