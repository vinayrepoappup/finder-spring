package com.example.finderspring.service;

public interface SingleEmailService {
    public String singleEmailVerify(String email);
    public String singleEmailFinder(String firstName,String lastName, String domain);
}
