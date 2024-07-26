package com.abin.chatserver.user.domain.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterReq {

    @NotBlank(message = "账号不能为空")
    private String username;

    @NotBlank
    @Length(min = 8, message = "密码不能少于8位")
    private String password;

    @NotBlank
    @Length(min = 8, message = "密码不能少于8位")
    private String confirmPassword;
}
