package com.example.week08.service;


import com.example.week08.domain.Member;
import com.example.week08.domain.Place;
import com.example.week08.domain.Post;
import com.example.week08.dto.request.PlacePutDto;
import com.example.week08.dto.request.PlaceRequestDto;
import com.example.week08.dto.request.PostPlacePutDto;
import com.example.week08.dto.request.PostRequestDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.PlaceRepository;
import com.example.week08.repository.PostRepository;
import com.example.week08.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.week08.errorhandler.ErrorCode.PLACE_NOT_EXIST;
import static com.example.week08.errorhandler.ErrorCode.POST_NOT_EXIST;


@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PostRepository postRepository;
    private final PlaceRepository placeRepository;
    private final S3Uploader s3Uploader;

    //카드 생성
    @Transactional
    public void placeCreate(Long courseId, PlaceRequestDto placeRequestDto, List<String> image, Member member)
            throws IOException
    {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("추천 코스가 존재하지 않습니다.", POST_NOT_EXIST)
        );
        String placeImage = null;
        //반복문으로 image리스트에 있는 값을 하나씩 빼서 place에 저장해줌
        for (int i = 0; i < image.size();) {
           placeImage = image.get(i);
           image.remove(i);
           break;
        }
        Place place = new Place(post, placeRequestDto, placeImage, member);
        placeRepository.save(place);
    }

    //카드 삭제
    @Transactional
    public void placeDelete( Long placeId, Member member) {
        Place place = placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessException("카드가 존재하지 않습니다.", ErrorCode.PLACE_NOT_EXIST)
        );

        if (!place.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        placeRepository.deleteById(placeId);
    }

    //카드 수정
    @Transactional
    public void placeUpdate(Long courseId, PlacePutDto placePutDto, List<String> image, Member member)throws IOException {
        Place place = placeRepository.findById(placePutDto.getPlaceId()).orElseThrow(() ->
                new BusinessException("카드가 존재하지 않습니다.", PLACE_NOT_EXIST)
        );
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("추천 코스가 존재하지 않습니다.", POST_NOT_EXIST)
        );
        if (!place.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        String imageUrl = place.getPlaceImage();
        //이미지 존재시 먼저 삭제후 다시 업로드.
        if (imageUrl != null) {
            String deleteUrl = imageUrl.substring(imageUrl.indexOf("/post/image"));
            s3Uploader.deleteImage(deleteUrl);
        }
        String placeImage = null;
        //반복문으로 image리스트에 있는 값을 하나씩 빼서 place에 저장해줌
        for (int i = 0; i < image.size();) {
            placeImage = image.get(i);
            image.remove(i);
            break;
        }
        place.update(placePutDto, post, placeImage, member);

    }


}
