package com.abin.chatserver.user.domain.vo.resp;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursorPageBaseResp<T> {

    @Schema(description = "游标（下次翻页带上这参数）")
    private String cursor;

    @Schema(description = "是否最后一页")
    private Boolean isLast = Boolean.FALSE;

    @Schema(description = "数据列表")
    private List<T> list;

    public static <T> CursorPageBaseResp<T> init(CursorPageBaseResp cursorPage, List<T> list) {
        return CursorPageBaseResp.<T>builder()
                .isLast(cursorPage.getIsLast())
                .list(list)
                .cursor(cursorPage.getCursor())
                .build();
    }

    @JsonIgnore
    public Boolean isEmpty() {
        return CollectionUtil.isEmpty(list);
    }

    public static <T> CursorPageBaseResp<T> empty() {
        return CursorPageBaseResp.<T>builder()
                .isLast(true)
                .list(new ArrayList<>())
                .build();
    }
}
