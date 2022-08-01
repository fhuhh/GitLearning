package com.example.server.pojo.functional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg {
    private String from;
    private String to;
    private String content;
    private LocalDateTime dateTime;
    private String fromNickname;
}
