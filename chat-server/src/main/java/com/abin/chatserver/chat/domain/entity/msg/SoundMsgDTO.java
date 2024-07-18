package com.abin.chatserver.chat.domain.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SoundMsgDTO extends BaseFileDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "时长（秒）")
    @NotNull
    private Integer second;
}
