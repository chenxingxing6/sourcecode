package com.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * User: lanxinghua
 * Date: 2019/11/4 15:34
 * Desc: 消息载体
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private String msgId;
    private String topic;
    private String tag;
    private String key;
    private Date storeTime;
    private String msgBody;
}
