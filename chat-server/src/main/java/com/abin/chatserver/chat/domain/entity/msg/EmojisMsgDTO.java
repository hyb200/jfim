package com.abin.chatserver.chat.domain.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmojisMsgDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "下载地址")
    @NotBlank
    private String url;
}


