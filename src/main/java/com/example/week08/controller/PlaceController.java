package com.example.week08.controller;



import com.example.week08.domain.UserDetailsImpl;
import com.example.week08.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class PlaceController {
    private final PlaceService placeService;

    //카드 삭제
    @DeleteMapping("/api/course/place/{placeId}")
    public ResponseEntity<String> deletePlace(@PathVariable Long placeId) {
        placeService.placeDelete(placeId);
        return ResponseEntity.ok("카드 삭제 성공");
    }
}
