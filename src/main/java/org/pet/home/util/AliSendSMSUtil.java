package org.pet.home.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public class AliSendSMSUtil {
    private  static  final Logger logger = LoggerFactory.getLogger(AliSendSMSUtil.class);
    public static String sendSms(String code, String phone){
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "GET";
        String appcode = "d99d2fa390cc464390cef535154ceffa";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:" + code);
        bodys.put("template_id", "TPL_0000");
        bodys.put("phone_number", phone);
        HttpResponse response = null;
        try {
            response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if (response == null){
            return  null;
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                result = StringUtil.convertStreamToString(inputStream);
                logger.info(result);
                return  result;
            } catch (IOException e) {
                // 处理异常
                logger.error(e.getMessage());
                return  null;
            }
        }
        return  null;
    }
}
