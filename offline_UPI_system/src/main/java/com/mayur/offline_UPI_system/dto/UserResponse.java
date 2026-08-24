package com.mayur.offline_UPI_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private int id;
    private String name;
    private String upiId;
    private String phoneNumber;
}