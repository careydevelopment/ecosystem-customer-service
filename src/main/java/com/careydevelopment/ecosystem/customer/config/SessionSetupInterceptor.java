package com.careydevelopment.ecosystem.customer.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.careydevelopment.ecosystem.customer.util.SessionUtil;

public class SessionSetupInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(SessionSetupInterceptor.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        sessionUtil.init(request);
        return true;
    }
 }
