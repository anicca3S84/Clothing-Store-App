package com.dailycode.clothingstore.data;

import com.dailycode.clothingstore.model.User;
import com.dailycode.clothingstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Transactional
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUserIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        for(int i = 1; i <= 5; i++){
            String defaultEmail = "user"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword("123456");
            userRepository.save(user);
        }
    }


}
