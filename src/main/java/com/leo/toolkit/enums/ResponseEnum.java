package com.leo.toolkit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseEnum {
    SUCCESS("0000", "success"),
    FAIL("9999", "fail");

    private final String code;
    private final String msg;


}
