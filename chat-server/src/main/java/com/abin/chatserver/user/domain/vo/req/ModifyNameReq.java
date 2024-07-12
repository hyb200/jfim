package com.abin.chatserver.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyNameReq {

    @NotNull
    @Length(max = 10, message = "用户名不能长于10位")
    @Schema(description = "用户名")
    private String name;
}
