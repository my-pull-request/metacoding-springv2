package com.metacoding.springv2.domain.user;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class UserRequest {

    @Data
    public static class UpdateDTO {
        @Email(message = "이메일 형식이 올바르지 않습니다")
        private String email;
        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(min = 8, max = 64, message = "비밀번호는 8~60자여야 합니다")
        private String password;
    }
}
