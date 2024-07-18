package com.abin.chatserver.chat.domain.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class BaseFileDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "大小（字节）")
    @NotNull
    private Long size;

    @Schema(description = "下载地址")
    @NotBlank
    private String url;
}
