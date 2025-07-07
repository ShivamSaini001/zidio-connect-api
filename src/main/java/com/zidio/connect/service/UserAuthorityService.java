package com.zidio.connect.service;

import java.util.List;

import com.zidio.connect.entities.UserAuthority;

public interface UserAuthorityService {

    public UserAuthority createAuthority(String name);
    public UserAuthority getAuthorityById(Long id);
    public UserAuthority getAuthorityByName(String name);
    public List<UserAuthority> getAllAuthorities();
    public UserAuthority deleteAuthorityById(Long id);

}
