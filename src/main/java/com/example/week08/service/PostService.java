package com.example.week08.service;

import com.example.week08.domain.Member;
import com.example.week08.domain.OpenWeatherData;
import com.example.week08.domain.Place;
import com.example.week08.domain.Post;
import com.example.week08.dto.request.*;
import com.example.week08.dto.response.PostResponseDto;
import com.example.week08.dto.response.PostResponseGetDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.repository.OpenWeatherDataRepository;
import com.example.week08.repository.PlaceRepository;
import com.example.week08.repository.PostRepository;
import com.example.week08.repository.PostSpecification;
import com.example.week08.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;
    private final PlaceService placeService;
    private final PlaceRepository placeRepository;
    private final OpenWeatherDataRepository openWeatherDataRepository;

    // 코스 게시글 작성(카드 이미지 통합)
    @Transactional
    public Post postCreate(PostPlaceDto postPlaceDto, List<MultipartFile> image, Member member)throws IOException {
        List<String> imgPaths  = s3Uploader.uploadList(image);
        System.out.println("IMG 경로들 : " + imgPaths);
        //uploadList에서 받은 이미지 경로 리스트를 하나씩 빼서 첫번째는 post에 나머지는 place에 하나씩 할당해줘야함
        String postImage = null;
        List<String> placeImage = new ArrayList<>(1);
            //만약 imgPaths의 길이가 0이면
            for (int i = 0; i < imgPaths.size(); i++) {
                if (i == 0) {
                    postImage = imgPaths.get(i);
                }else{
                    placeImage.add(i-1, imgPaths.get(i));

                }
            }


        Post post = new Post(postPlaceDto.getPostRequestDto(), postImage, member);
        Long courseId = postRepository.save(post).getId();
        for (int i =0; i <postPlaceDto.getPlaceRequestDtoList().size(); i++){
            placeService.placeCreate(courseId, postPlaceDto.getPlaceRequestDtoList().get(i), placeImage, member);
        }
        return post;
        }

    // 코스(게시글) 단건 조회
    @Transactional
    public PostResponseDto getPost(Long courseId) {
        Post post = postRepository.findByJoinPlace(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        return new PostResponseDto(post);
    }

    // 코스(게시글) 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponseGetDto> getAllPost() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream()
                .map(PostResponseGetDto::new)
                .collect(Collectors.toList());
    }

    // 코스(게시글) 카테고리 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> findPost(PostRequestDto requestDto) {
        Map<String, Object> searchKeys = new HashMap<>();
        if (requestDto.getWeather() != null) searchKeys.put("weather", requestDto.getWeather());
        if (requestDto.getRegion() != null) searchKeys.put("region", requestDto.getRegion());
        if (requestDto.getSeason() != null) searchKeys.put("season", requestDto.getSeason());
        if (requestDto.getWho() != null) searchKeys.put("who", requestDto.getWho());

        return postRepository.findAll(PostSpecification.searchPost(searchKeys))
                .stream()
                .map(p -> new PostResponseDto((Post) p))
                .collect(Collectors.toList());
    }

    // 코스(게시글) 검색(제목, 내용, 카테고리 검색)
    @Transactional
    public List<PostResponseDto> searchPost(String keyword){
        return postRepository.findAllSearch(keyword)
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 코스 게시글 수정(카드 이미지 통합)
    @Transactional
    public Post postUpdate(Long courseId, PostPlacePutDto postPlacePutDto, List<MultipartFile> image, Member member) throws IOException {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }


        String imageUrl = post.getImage();
        //이미지 존재시 먼저 삭제후 다시 업로드.
        if (imageUrl != null) {
            String deleteUrl = imageUrl.substring(imageUrl.indexOf("/post/image"));
            s3Uploader.deleteImage(deleteUrl);
        }
        List<String> imgPaths  = s3Uploader.uploadList(image);
        System.out.println("IMG 경로들 : " + imgPaths);

        String postImage = null;
        List<String> placeImage = new ArrayList<>(1);
        //만약 imgPaths의 길이가 0이면
        for (int i = 0; i < imgPaths.size(); i++) {
            if (i == 0) {
                postImage = imgPaths.get(i);
            }else{
                placeImage.add(i-1, imgPaths.get(i));

            }
        }
        post.update(postPlacePutDto.getPostRequestDto(), postImage, member);
        for (int i =0; i <postPlacePutDto.getPlacePutDtoList().size(); i++){

            PlacePutDto place = postPlacePutDto.getPlacePutDtoList().get(i);
            placeService.placeUpdate(courseId, place, placeImage, member);
        }

        return post;
    }

    // 코스(게시글) 삭제(카드이미지 삭제 통합)
    @Transactional
    public void postDelete(Long courseId, PlaceDeleteDto placeDeleteDto , Member member) throws IOException {
        Post post = postRepository.findById(courseId).orElseThrow(
                () -> new BusinessException("존재하지 않는 게시글 id 입니다.", ErrorCode.POST_NOT_EXIST)
        );
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        for (int i = 0; i < placeDeleteDto.getPlaceId().size(); i++) {
            Place place = placeRepository.findById(placeDeleteDto.getPlaceId().get(i)).orElseThrow(() ->
                    new BusinessException("카드가 존재하지 않습니다.", ErrorCode.PLACE_NOT_EXIST)
            );
            String imageUrlPlace = place.getPlaceImage();
            if (imageUrlPlace != null) {
                String deleteUrl = imageUrlPlace.substring(imageUrlPlace.indexOf("/post/image"));
                s3Uploader.deleteImage(deleteUrl);
                placeRepository.deleteById(placeDeleteDto.getPlaceId().get(i));
            }
        }

        String imageUrl = post.getImage();
        if (imageUrl != null) {
            String deleteUrl = imageUrl.substring(imageUrl.indexOf("/post/image"));
            s3Uploader.deleteImage(deleteUrl);
        }

        postRepository.deleteById(courseId);
    }

//    // 메인 새로운게시물/날씨/지역/계절/평점 기반 (회원용)
//    @Transactional(readOnly = true)
//    public List<PostResponseGetDto> getRecommended(Member member) {
//
//        Optional<OpenWeatherData> openWeatherData = openWeatherDataRepository.findByMember(member);
//        Map<String, Object> searchKeys = new HashMap<>();
//        searchKeys.put("newPost", true); //새로운 게시물
//        //지역이 'wonju' 처럼 들어오면 '강원'으로 바꿔서 해시맵에 넣어준다
//        if (openWeatherData.get().getRegion() != null) searchKeys.put("region", openWeatherData.get().getRegion());//지역
//        //CAPITAL("수도권"), 서울 Seoul 인천 incheon
//        // 수원 suwon 용인 yongin 성남 seongnam 부천 bucheon 화성 Hwaseong 안산 Ansan 안양 Anyang 평택 Pyeongtaek 시흥 Siheung
//        // 김포 gimpo 광주 Gwangju 광명 Gwangmyeong 강화 ganghwa 군포 Gunpo 하남 Hanam 오산 Osan 이천 Icheon
//        // 안성 Anseong 의왕 Uiwang 양평 Yangpyeong 여주 Yeoju 과천 Gwacheon 고양 Goyang 남양주 Namyangju 파주 paju
//        // 의정부 Uijeongbu 양주 Yangju 구리 Guri 포천 Pocheon 동두천 Dongducheon 가평 Gapyeong 연천 Yeoncheon
//
//        // GANGWON("강원") 춘천 Chuncheon 원주 Wonju 강릉 Gangneung 동해 Donghae 태백 Taebaek 속초 Sokcho 삼척 Samcheok
//        // 홍천 Hongcheon 횡성 Hoengseong 영월 Yeongwol 평창 Pyeongchang 정선 Jeongseon 철원 Cheorwon 화천 Hwacheon 양구 Yanggu
//        // 인제 Inje 고성 Goseong 양양 Yangyang
//
//        // CHUNGBUK("충북")
//        //청주 Cheongju 충주 Chungju 제천 Jecheon 보은 Boeun
//        // CHUNGNAM("충남")
//        // JEONBUK("전북")
//        // JEONNAM("전남")
//        // GYEONGBUK("경북")
//        // GYEONGNAM("경남")
//        // JEJU("제주")
//
////        if (openWeatherData.get().getWeather() != null) searchKeys.put("weather", openWeatherData.get().getWeather());//날씨
////        if (openWeatherData.get().getRegion() != null) searchKeys.put("region", openWeatherData.get().getRegion());//지역
//        if (openWeatherData.get().getSeason() != null) searchKeys.put("season", openWeatherData.get().getSeason());//계절
//        System.out.println(searchKeys);
//        searchKeys.put("avgScore", topAvgScore());//평점
//
//        return postRepository.findAll(PostSpecification.searchPost(searchKeys))
//                .stream()
//                .map(PostResponseGetDto::new)
//                .collect(Collectors.toList());
//    }
    //메인 비로그인 유저용 새로운게시물/ 평점기반
    @Transactional(readOnly = true)
    public List<PostResponseGetDto> getCommonRecommended() {

        Map<String, Object> searchKeys = new HashMap<>();
        searchKeys.put("newPost", true); //새로운 게시물
        searchKeys.put("avgScore", topAvgScore());//평점

        return postRepository.findAll(PostSpecification.searchPost(searchKeys))
                .stream()
                .map(PostResponseGetDto::new)
                .collect(Collectors.toList());
    }

    //평점 평균 최대값
    @Transactional
    public int topAvgScore(){
        //리스트가 아니라 가장 높은 avgScore값을 가져와야함
        List<Post> post = postRepository.findAll();
        List<Integer> avgPostList = new ArrayList<>();
        for (int i = 0; i < post.size(); i++){
        int avgScores = post.get(i).getAvgScore();
            avgPostList.add(i, avgScores);
        }

        int top = avgPostList.stream().max(Integer::compare).orElse(-1);
    return top;
    }

}