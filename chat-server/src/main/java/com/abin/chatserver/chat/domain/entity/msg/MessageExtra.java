package com.abin.chatserver.chat.domain.entity.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageExtra implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

//    //url跳转链接
//    private Map<String, UrlInfo> urlContentMap;

    /**
     * 消息撤回详情
     */
    private MsgRecall recall;

    /**
     * 艾特的 uid
     */
    private List<Long> atUidList;

    /**
     * 文件消息
     */
    private FileMsgDTO fileMsgDTO;

    /**
     * 图片消息
     */
    private ImgMsgDTO imgMsgDTO;

    /**
     * 语音消息
     */
    private SoundMsgDTO soundMsgDTO;

    /**
     * 视频消息
     */
    private VideoMsgDTO videoMsgDTO;

    /**
     * 表情图片信息
     */
    private EmojisMsgDTO emojisMsgDTO;
}
