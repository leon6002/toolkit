package com.leo.toolkit.controller;

import com.leo.toolkit.model.ResponseModel;
import com.leo.toolkit.utils.DateKit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/date")
public class DateController {

    @GetMapping("/inRange")
    public ResponseModel<List<String>> listDateInBetween(String startDate, String endDate) {
        List<String> dateList = DateKit.getDateList(startDate, endDate);
        return new ResponseModel<>(dateList);
    }
}
