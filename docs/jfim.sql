DROP TABLE IF EXISTS `user_emoji`;
CREATE TABLE `user_emoji`
(
    `id`            bigint(20) unsigned                     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `uid`           bigint(20)                              NOT NULL COMMENT '用户表ID',
    `emoji_url`     varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表情地址',
    `delete_status` int(1)                                  NOT NULL DEFAULT '0' COMMENT '逻辑删除(0-正常,1-删除)',
    `create_time`   datetime(3)                             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`   datetime(3)                             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `IDX_USER_EMOJIS_UID` (`uid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = DYNAMIC COMMENT ='用户表情包';

DROP TABLE IF EXISTS `session`;
CREATE TABLE `session`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `type`        int             NOT NULL COMMENT '会话类型 1群聊 2单聊',
    `hot_flag`    int                      DEFAULT '0' COMMENT '是否为热点群聊',
    `active_time` datetime(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '群消息的最后更新时间（热点群不需要写扩散，只更新这里）',
    `last_msg_id` bigint                   DEFAULT NULL COMMENT '会话中的最后一条消息id',
    `ext_json`    json                     DEFAULT NULL COMMENT '额外信息（根据不同类型房间有不同存储的东西）',
    `create_time` datetime(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_create_time` (`create_time`) USING BTREE,
    KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='会话表';


DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `uid`         bigint(20)          NOT NULL COMMENT 'uid',
    `session_id`  bigint(20)          NOT NULL COMMENT '会话id',
    `read_time`   datetime(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '阅读到的时间',
    `active_time` datetime(3)                  DEFAULT NULL COMMENT '会话内消息最后更新的时间(只有普通会话需要维护，全员会话不需要维护)',
    `last_msg_id` bigint(20)                   DEFAULT NULL COMMENT '会话最新消息id',
    `create_time` datetime(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_uid_room_id` (`uid`, `session_id`) USING BTREE,
    KEY `idx_room_id_read_time` (`session_id`, `read_time`) USING BTREE,
    KEY `idx_create_time` (`create_time`) USING BTREE,
    KEY `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='会话列表';

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`           bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `session_id`   bigint(20)          NOT NULL COMMENT '会话表id',
    `from_uid`     bigint(20)          NOT NULL COMMENT '消息发送者uid',
    `content`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息内容',
    `reply_msg_id` bigint(20)          NULL                                       DEFAULT NULL COMMENT '回复的消息内容',
    `status`       int(11)             NOT NULL COMMENT '消息状态 0正常 1删除',
    `gap_count`    int(11)             NULL                                       DEFAULT NULL COMMENT '与回复的消息间隔多少条',
    `type`         int(11)             NULL                                       DEFAULT 1 COMMENT '消息类型 1正常文本 2.撤回消息',
    `extra`        json                                                           DEFAULT NULL COMMENT '扩展信息',
    `create_time`  datetime(3)         NOT NULL                                   DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`  datetime(3)         NOT NULL                                   DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_room_id` (`session_id`) USING BTREE,
    INDEX `idx_from_uid` (`from_uid`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE,
    INDEX `idx_update_time` (`update_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '消息表'
  ROW_FORMAT = Dynamic;

CREATE TABLE `secure_invoke_record`
(
    `id`                 bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `secure_invoke_json` json                NOT NULL COMMENT '请求参数',
    `status`             tinyint(8)          NOT NULL COMMENT '状态 1待执行 2已失败',
    `next_retry_time`    datetime(3)         NOT NULL COMMENT '下一次重试的时间',
    `retry_times`        int(11)             NOT NULL COMMENT '已经重试次数',
    `max_retry_times`    int(11)             NOT NULL COMMENT '最大重试次数',
    `fail_reason`        text COMMENT '执行失败的堆栈',
    `create_time`        datetime(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`        datetime(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_next_retry_time` (`next_retry_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='本地消息表';

