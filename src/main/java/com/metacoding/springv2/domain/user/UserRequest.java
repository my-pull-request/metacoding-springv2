package com.metacoding.springv2.domain.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class UpdateDTO {
        private String email;
        private String password;
    }
}
