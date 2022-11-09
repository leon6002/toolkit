package com.leo.toolkit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {
    @PostMapping("export")
    public String excel(){
        return "";
    }
}
