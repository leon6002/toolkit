package com.leo.toolkit.controller;

import com.leo.toolkit.model.ResponseModel;
import com.leo.toolkit.utils.RequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @GetMapping("/ip")
    public ResponseModel<String> ip(HttpServletRequest request) {
        String clientIpAddr = RequestUtils.getClientIpAddr(request);
        return new ResponseModel<>(clientIpAddr);
    }
}
