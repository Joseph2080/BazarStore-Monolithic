package org.bazar.bazarstore_v2.domain.order;

import java.util.Map;

public class ApiResponseDto {
    private String message;
    private Map<String, Object> data;

    public ApiResponseDto() {}

    public ApiResponseDto(String message, Map<String, Object> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
