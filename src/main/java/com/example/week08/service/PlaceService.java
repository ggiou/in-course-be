package com.example.week08.service;


import com.example.week08.domain.Member;
import com.example.week08.domain.Place;
import com.example.week08.domain.Post;
import com.example.week08.dto.request.PlacePutDto;
import com.example.week08.dto.request.PlaceRequestDto;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.repository.PlaceRepository;
import com.example.week08.repository.PostRepository;
import com.example.week08.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

import static com.example.week08.errorhandler.ErrorCode.PLACE_NOT_EXIST;


@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PostRepository postRepository;
    private final PlaceRepository placeRepository;
    private final S3Uploader s3Uploader;

    //카드 생성
    @Transactional
    public void placeCreate(Long courseId, PlaceRequestDto placeRequestDto
//            , Member member
    )
//            throws IOException
    {
//        String imageDefault = "https://dh-s3-bucket01.s3.ap-northeast-2.amazonaws.com/%EB%94%94%ED%8F%B4%ED%8A%B8%EC%9D%B4%EB%AF%B8%EC%A7%80.png";

//        Post post = postRepository.findById(placeRequestDto.getPostId()).orElseThrow(
//                () -> new BusinessException("추천 코스가 존재하지 않습니다.", POST_NOT_EXIST)
//        ); //카드가 작성되는 시점에 포스트가 존재할 수 없음(같이 작성되기 때문)!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//        if (placeRequestDto.getImage() == null){
//            placeRequestDto.setImage(imageDefault);
//        }
//        String placeImage = s3Uploader.upload(image, "static");

        Place place = new Place(courseId, placeRequestDto
//                , member
        );
        placeRepository.save(place);
    }

    //카드 삭제
    @Transactional
    public void placeDelete(Long placeId
//            , Member member
    ) {
        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessException("카드가 존재하지 않습니다.", PLACE_NOT_EXIST)
        );
//        if (!place.getMember().getId().equals(member.getId())) {
//            throw new IllegalArgumentException("수정 권한이 없습니다.");
//        }
        placeRepository.deleteById(placeId);
    }

    //카드 수정
    @Transactional
    public void placeUpdate(PlacePutDto placePutDto
//            , Member member
    ) {
        Place place = placeRepository.findById(placePutDto.getPlaceId()).orElseThrow(() ->
                new BusinessException("카드가 존재하지 않습니다.", PLACE_NOT_EXIST)
        );
//        if (!place.getMember().getId().equals(member.getId())) {
//            throw new IllegalArgumentException("수정 권한이 없습니다.");
//        }

        place.update(placePutDto);
    }

}
