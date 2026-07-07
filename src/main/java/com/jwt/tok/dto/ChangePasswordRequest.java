package com.jwt.tok.dto;

import lombok.Data;

//@Data
//public class ChangeCredentialsRequest {
//    private String username;
//    private String currentPassword;
//    private String newPassword;
//
//    private String role; // Admin / Dealer / Employee
//    private Long userId; // Admin use case
//}

@Data
public class ChangePasswordRequest {

    private String username;   // self update साठी
    private Long userId;       // admin case साठी
    private String newPassword;

    // getters setters
}