package com.mayur.offline_UPI_system.model;

import lombok.Getter;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private int id;
    private String Name;
    private String UPIid;
    private String phoneNumber;
}
