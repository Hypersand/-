package com.mysite.sbb.user;


import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String password, String email) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setQuestion(null);
        userRepository.save(user);

        return user;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        throw new DataNotFoundException("siteuser not found");
    }

//    public Page<Question> getUserQuestion(String username) {
//
//    }


}
