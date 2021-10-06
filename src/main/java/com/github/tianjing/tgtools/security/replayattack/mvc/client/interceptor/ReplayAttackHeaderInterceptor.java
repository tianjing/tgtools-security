package com.github.tianjing.tgtools.security.replayattack.mvc.client.interceptor;

import com.github.tianjing.tgtools.encrypt.EncrypterFactory;
import com.github.tianjing.tgtools.encrypt.spring.security.DelegatingEncrypter;
import com.github.tianjing.tgtools.security.replayattack.mvc.bean.ReplayAttackConfigProperty;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import tgtools.json.JSONException;
import tgtools.json.JSONObject;
import tgtools.util.StringUtil;

/**
 * @author 田径
 * @date 2021-03-22 10:26
 * @desc
 **/
public class ReplayAttackHeaderInterceptor implements RequestInterceptor {

    protected DelegatingEncrypter delegatingEncrypter;

    @Autowired(required = false)
    protected ReplayAttackConfigProperty replayAttackConfigProperty;

    protected String encryptId;

    protected ReplayAttackConfigProperty getReplayAttackConfigProperty() {
        if (null == replayAttackConfigProperty) {
            replayAttackConfigProperty = new ReplayAttackConfigProperty();
            replayAttackConfigProperty.init();
        }

        return replayAttackConfigProperty;
    }


    protected DelegatingEncrypter getDelegatingEncrypter() {
        try {
            delegatingEncrypter = tgtools.web.platform.Platform.getBeanFactory().getBean(DelegatingEncrypter.class);
        } catch (Throwable e) {
            delegatingEncrypter = EncrypterFactory.createDelegatingEncrypter(getReplayAttackConfigProperty().getEncryptId(), getReplayAttackConfigProperty().getEncryptKey());
        }
        if (StringUtil.isEmpty(encryptId)) {
            encryptId = "{" + replayAttackConfigProperty.getEncryptId() + "}";
        }
        return delegatingEncrypter;
    }


    @Override
    public void apply(RequestTemplate template) {
        JSONObject vJson = new JSONObject();
        try {
            vJson.put("", tgtools.util.DateUtil.formatFullLongtime(tgtools.util.DateUtil.getCurrentDate()));
            String vWord = this.getDelegatingEncrypter().encode(vJson.toString());
            vWord = StringUtil.replace(vWord, encryptId, "");
            template.header("SECURITY-TOKEN", vWord);
        } catch (JSONException e) {
        }
    }
}
