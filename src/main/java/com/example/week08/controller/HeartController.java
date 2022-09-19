//package com.example.week08.controller;
//
//import com.example.week08.dto.response.ResponseDto;
//import com.example.week08.service.HeartService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Validated
//@RequiredArgsConstructor
//@RestController
//public class HeartController {
//    private final HeartService heartService;
//
//    @PostMapping( "/api/course/heart/{courseId}")
//    public ResponseDto<?> addPostHeart(@PathVariable Long courseId, HttpServletRequest request) {
//        return heartService.addPostHeart(courseId, request);
//    }
//
//}
//
//
//
//
//
