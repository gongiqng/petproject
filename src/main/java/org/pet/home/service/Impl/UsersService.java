package org.pet.home.service.Impl;

import org.pet.home.dao.UsersMapper;
import org.pet.home.entity.Users;
import org.pet.home.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Service
public class UsersService implements IUsersService {
    private UsersMapper usersMapper;

    @Autowired
    public UsersService(UsersMapper usersMapper){
        this.usersMapper = usersMapper;
    }
    @Override
    public int register(Users user) {
        return usersMapper.register(user);
    }

    @Override
    public Users getUser(String phone, String password) {
        return usersMapper.getUser(phone,password);
    }

    @Override
    public Users getAdmin(String phone, String password) {
        return usersMapper.getAdmin(phone,password);
    }

    @Override
    public Users selectPhone(String phone) {
        return usersMapper.selectPhone(phone);
    }
}
