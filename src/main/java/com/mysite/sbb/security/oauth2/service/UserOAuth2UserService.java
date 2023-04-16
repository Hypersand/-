package com.mysite.sbb.security.oauth2.service;

import com.mysite.sbb.security.oauth2.CustomOAuth2User;
import com.mysite.sbb.security.oauth2.userInfo.KakaoOAuth2UserInfo;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.user.UserService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoOAuth2UserInfo kakaoOAuth2UserInfo = new KakaoOAuth2UserInfo((Map<String, Object>) oAuth2User.getAttribute("kakao_account"));
        String name = oAuth2User.getName();
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String username = providerTypeCode + name;

        SiteUser siteUser;

        Optional<SiteUser> optionalSiteUser = userRepository.findByUsername(username);
        if (optionalSiteUser.isEmpty()) {
            siteUser = userService.create(username, "", kakaoOAuth2UserInfo.getEmail());
        }

        else {
            siteUser = optionalSiteUser.get();
        }

        String role = siteUser.getUserRole().getValue();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(grantedAuthority);


        return new CustomOAuth2User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}

