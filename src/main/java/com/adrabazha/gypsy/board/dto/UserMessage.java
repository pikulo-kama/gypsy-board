package com.adrabazha.gypsy.board.dto;

import com.adrabazha.gypsy.board.exception.UserMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class UserMessage {

    public static final String ERROR_FLASH_ATTRIBUTE = "error";

    private String message;

    private UserMessageType userMessageType;

    private Map<String, Object> responseData;

    public static UserMessage success(String message) {
        return new UserMessage(message, UserMessageType.SUCCESS, new HashMap<>());
    }

    public static UserMessage error(String message) {
        return new UserMessage(message, UserMessageType.ERROR, new HashMap<>());
    }

    public void addResponseDataEntry(String key, Object value) {
        responseData.putIfAbsent(key, value);
    }
}
