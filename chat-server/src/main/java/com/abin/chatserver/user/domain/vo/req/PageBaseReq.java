package com.abin.chatserver.user.domain.vo.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
@Schema(description = "基础翻页请求")
public class PageBaseReq {

    @Schema(description = "当前索引")
    private Integer pageNo = 1;

    @Schema(description = "页面大小")
    @Max(value = 50, message = "一页最多支持50条消息")
    private Integer pageSize = 50;

    /**
     * 获取 mybatis-plus 的 page
     * @return
     */
    public Page plusPage() {
        return new Page(pageNo, pageSize);
    }
}
