package com.abin.chatserver.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadUrlReq {

    @Schema(description = "文件名（带后缀）")
    @NotBlank
    private String fileName;

    @Schema(description = "上传场景 1.聊天信息；2.表情包")
    @NotNull
    private Integer scene;
}
