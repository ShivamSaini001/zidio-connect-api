package com.zidio.connect.service;

import com.zidio.connect.dto.ProfileDto;
import com.zidio.connect.dto.UserDto;

public interface ProfileManagerService {

    <T extends ProfileDto> T createUserProfile(String email, T profileDto);
    <T extends ProfileDto> T updateUserProfile(String email, T updatedProfileDto);
    <T extends ProfileDto> T getUserProfile(UserDto userDto, Class<T> profileType);
    void deleteUserProfile(String email);
}
