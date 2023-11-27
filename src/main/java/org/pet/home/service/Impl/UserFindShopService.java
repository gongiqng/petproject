package org.pet.home.service.Impl;

import org.pet.home.dao.UserMsgMapper;
import org.pet.home.entity.UserFindShop;
import org.pet.home.net.param.UserFindShopParam;
import org.pet.home.service.IUserFindShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Service
public class UserFindShopService implements IUserFindShopService {
    private UserMsgMapper userMsgMapper;

    @Autowired
    public UserFindShopService(UserMsgMapper userMsgMapper){
        this.userMsgMapper = userMsgMapper;
    }
    @Override
    public int add(UserFindShop userMsg) {
        return userMsgMapper.add(userMsg);
    }

    @Override
    public int addTask(long shopId, long employeeId, long petId, long userId, long userMsgId) {
        return userMsgMapper.addTask(shopId, employeeId, petId, userId, userMsgId);
    }

    @Override
    public List<UserFindShop> getPetListByState(int state) {
        return userMsgMapper.getPetListByState(state);
    }

    @Override
    public List<UserFindShop> getUserList(long userId) {
        return userMsgMapper.getUserList(userId);
    }

    @Override
    public List<UserFindShop> getShoplist(long shopId) {
        return userMsgMapper.getShoplist(shopId);
    }

    @Override
    public List<Long> findAllShopId() {
        return userMsgMapper.findAllShopId();
    }

    @Override
    public List<UserFindShop> getShopList(long shopId) {
        return userMsgMapper.getShopList(shopId);
    }

    @Override
    public int updateState(int state, long id) {
        return  userMsgMapper.updateState(state,id);
    }

    @Override
    public int insert(UserFindShopParam userFindShopParam) {
        return userMsgMapper.insert(userFindShopParam);
    }

    @Override
    public UserFindShop findById(long id) {
        return userMsgMapper.findById(id);
    }
}
