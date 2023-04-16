package com.mysite.sbb.user;


import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    @Transactional
    public SiteUser create(String username, String password, String email) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setUserRole(setAuthorities(username));
        userRepository.save(user);

        return user;
    }

    public UserRole setAuthorities(String username) {
        if (username.equals("admin")) {
            return UserRole.ADMIN;
        }

        return UserRole.USER;
    }



    public SiteUser getUser(String username) {
        Optional<SiteUser> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        throw new DataNotFoundException("siteuser not found");
    }

    public MailForm createMailForm(String email) {
        String password = createRandomPassword();
        updateTempPassword(password, email);

        MailForm mailForm = new MailForm();
        mailForm.setAddress(email);
        mailForm.setTitle("임시비밀번호 안내 메일입니다.");
        mailForm.setMessage("회원님의 임시 비밀번호는 " + password + "입니다. 감사합니다");

        return mailForm;
    }


    @Transactional
    public void updateTempPassword(String password, String email) {
        SiteUser user = userRepository.findByEmail(email).get();
        user.setPassword(passwordEncoder.encode(password));
    }

    @Transactional
    public void updatePassword(String password, SiteUser user) {
        user.setPassword(passwordEncoder.encode(password));
    }


    public String createRandomPassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append((int) (Math.random() * 10));
        }

        return sb.toString();
    }

    public void sendEmail(MailForm mailForm) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailForm.getAddress());
        message.setSubject(mailForm.getTitle());
        message.setText(mailForm.getMessage());
        message.setFrom("on8214@naver.com");
        message.setReplyTo("on8214@naver.com");
        mailSender.send(message);
    }



}
