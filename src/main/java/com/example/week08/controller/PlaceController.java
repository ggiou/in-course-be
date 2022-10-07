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

    //카드 생성
//    @PostMapping("/api/course/place/")
//    public Place createPlace(PlaceRequestDto placeRequestDto,
//                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
//        return placeService.placeCreate(placeRequestDto, userDetails.getMember());
//    }

    //카드 삭제
    @DeleteMapping("/api/course/place/{placeId}")
    public ResponseEntity<String> deletePlace(@PathVariable Long placeId
    )
    {
        placeService.placeDelete(placeId
        );
        return ResponseEntity.ok("카드 삭제 성공");
    }
//    //카드 수정
//    @PutMapping("/api/course/place/{placeId}")
//    public ResponseEntity<String> deletePlace(@RequestBody PlacePutDto placePutDto,
//                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        placeService.placeUpdate(placePutDto, userDetails.getMember());
//
//        return ResponseEntity.ok("카드 변경 성공");
//    }
}
