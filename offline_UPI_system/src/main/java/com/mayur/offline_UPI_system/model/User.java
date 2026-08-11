package com.mayur.offline_UPI_system.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String upiId;
    private String phoneNumber;

}
