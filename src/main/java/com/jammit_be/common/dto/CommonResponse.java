package com.jammit_be.common.dto;

import com.jammit_be.common.contant.JammitConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.jammit_be.common.contant.JammitConstants.HttpConstant.SUCCESS_CODE;
import static com.jammit_be.common.contant.JammitConstants.HttpConstant.SUCCESS_MESSAGE;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    @Schema(description = "성공 여부")
    private Boolean success;
    @Schema(description = "결과 코드")
    private Integer code;
    @Schema(description = "결과 메세지")
    private String message;
    @Schema(description = "결과 데이터")
    private T result;

    public static <T> CommonResponse<T> ok() {
        return CommonResponse.<T>builder()
                .success(true)
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .result(null)
                .build();
    }


    public static <T> CommonResponse<T> ok(T result) {
        return CommonResponse.<T>builder()
                .success(true)
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .result(result)
                .build();
    }

    public CommonResponse<T> success(T result) {
        return CommonResponse.<T>builder()
                .success(true)
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .result(result)
                .build();
    }

    public CommonResponse<T> fail(Integer code, String message) {
        return CommonResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> fail(int code, String message, T result) {
        return CommonResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .result(result)
                .build();
    }
}
