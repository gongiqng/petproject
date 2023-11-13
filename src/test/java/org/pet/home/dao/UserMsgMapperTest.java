package org.pet.home.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pet.home.entity.UserMsg;
import org.pet.home.util.GaoDeMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMsgMapperTest {
    @Autowired
    public  UserMsgMapper userMsgMapper;
    @Test
    public void testAdd() throws UnsupportedEncodingException {
        UserMsg userMsg = new UserMsg();
        userMsg.setAddress(Objects.requireNonNull(GaoDeMapUtil.getLngAndLag("湖北省武汉市洪山区光谷广场")).getFormattedAddress());
        userMsg.setAdminId(317L);
        userMsg.setShopId(30L);
        userMsg.setName("小黑");
        userMsg.setPetId(3L);
        userMsg.setPrice(1000.0);
        userMsg.setUserId(5L);
        userMsg.setBirth(System.currentTimeMillis());
        userMsg.setIsinoculation(1);
        userMsg.setSex("雄");
        userMsg.setState(1);
        userMsg.setCreatetime(System.currentTimeMillis());
        userMsgMapper.add(userMsg);
    }
}

