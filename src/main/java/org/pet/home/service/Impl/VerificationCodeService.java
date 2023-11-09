package org.pet.home.service.Impl;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
//用于生成和保存验证码
@Service
public class VerificationCodeService {
    private Map<String, String> verificationCodes = new HashMap<>();

    public String generateCode() {
        // 生成验证码逻辑
        String code = "1234"; // 这里假设生成的验证码为1234
        return code;
    }

    public void saveCode(String username, String code) {
        verificationCodes.put(username, code);
    }

    public String getCodeByUsername(String username) {
        return verificationCodes.get(username);
    }
}
