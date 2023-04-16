package com.mysite.sbb.security.oauth2.userInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    private Map<String, Object> attributes;



    public String getEmail() {
        return (String) attributes.get("email");
    }

}
