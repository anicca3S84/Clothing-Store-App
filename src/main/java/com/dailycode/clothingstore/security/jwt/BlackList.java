package com.dailycode.clothingstore.security.jwt;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class BlackList {
    private final Set<String> set = new HashSet<>();

    public void addToBlackList(String token){
        set.add(token);
    }

    public boolean isInBlackList(String token){
        return set.contains(token);
    }
}
