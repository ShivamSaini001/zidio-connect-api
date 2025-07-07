package com.zidio.connect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zidio.connect.entities.UserAuthority;
import com.zidio.connect.service.UserAuthorityService;

@RestController
@RequestMapping("/api/v1/role")
public class AuthorityController {
    @Autowired
    UserAuthorityService userAuthorityService;
    
    @PostMapping("/add/{name}")
    public ResponseEntity<UserAuthority> addAuthority(@PathVariable String name){
        UserAuthority userAuthority = userAuthorityService.createAuthority(name);
        return new ResponseEntity<UserAuthority>(userAuthority, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserAuthority> getAuthorityById(@PathVariable Long id){
        UserAuthority userAuthority = userAuthorityService.getAuthorityById(id);
        return new ResponseEntity<UserAuthority>(userAuthority, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<UserAuthority>> getAllAuthorities(){
        List<UserAuthority> userAuthorities = userAuthorityService.getAllAuthorities();
        return new ResponseEntity<List<UserAuthority>>(userAuthorities, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserAuthority> deleteAuthorityById(@PathVariable Long id){
        UserAuthority userAuthority = userAuthorityService.deleteAuthorityById(id);
        return new ResponseEntity<UserAuthority>(userAuthority, HttpStatus.OK);
    }
}
