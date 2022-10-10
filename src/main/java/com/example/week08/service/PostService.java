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
import com.example.week08.repository.*;
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
    private final HeartRepository heartRepository;
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
            placeService.placeCreate(courseId, postPlaceDto.getPlaceRequestDtoList().get(i), placeImage);
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
    public List<PostResponseDto> getAllPost() {
        return postRepository.findAllByOrderByModifiedAtDesc()
                .stream()
                .map(PostResponseDto::new)
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
                .map(PostResponseDto::new)
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
            placeService.placeUpdate(courseId, place, placeImage);
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
        heartRepository.deleteAllByPostId(courseId);
    }

    // 메인 새로운게시물/날씨/지역/계절/평점 기반 (회원용)
    @Transactional(readOnly = true)
    public Optional<PostResponseGetDto> getRecommended(Member member) {

        Optional<OpenWeatherData> openWeatherData = openWeatherDataRepository.findByMember(member);
        Map<String, Object> searchKeys = new HashMap<>();
        searchKeys.put("newPost", true); //새로운 게시물
        //지역이 'Wonju' 처럼 들어오면 '강원'으로 바꿔서 해시맵에 넣어준다
        if (openWeatherData.get().getWeather() != null) searchKeys.put("weather", openWeatherData.get().getWeather());//날씨
        if (openWeatherData.get().getRegion() != null || regionChange(openWeatherData.get().getRegion()) != null)
            searchKeys.put("region", regionChange(openWeatherData.get().getRegion()));//지역
        if (openWeatherData.get().getSeason() != null) searchKeys.put("season", openWeatherData.get().getSeason());//계절

        Comparator<PostResponseGetDto> scoreComparator = Comparator.comparingInt(PostResponseGetDto::getAvgScore);

        return postRepository.findAll(PostSpecification.searchPost(searchKeys))
                .stream()
                .map(PostResponseGetDto::new)
                .collect(Collectors.maxBy(scoreComparator));
    }
    //메인 비로그인 유저용 새로운게시물/ 평점기반
    @Transactional(readOnly = true)
    public Optional<PostResponseGetDto> getCommonRecommended() {

        Map<String, Object> searchKeys = new HashMap<>();
        searchKeys.put("newPost", true); //새로운 게시물

        Comparator<PostResponseGetDto> scoreComparator = Comparator.comparingInt(PostResponseGetDto::getAvgScore);
        return postRepository.findAll(PostSpecification.searchPost(searchKeys))
                .stream()
                .map(PostResponseGetDto::new)
                .collect(Collectors.maxBy(scoreComparator));
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


    //들어온 지역값을 카테고리에 맞게 바꿔서 돌려보내준다
    @Transactional
    public String regionChange(String region){

        String[] capital = {"Seoul","Seŏul","Incheon","Incheŏn","Suwon","Suwŏn","Yongin","Yŏngin","Seongnam","Seŏngnam",
                "Bucheon","Bucheŏn","Hwaseong","Hwaseŏng","Ansan","Anyang","Pyeongtaek","Pyeŏngtaek","Siheung","Gimpo",
                "Gimpŏ","Gwangju","Gwangmyeong","Gwangmyeŏng","Gunpo","Gunpŏ","Hanam","Osan","Ŏsan","Icheon","Icheŏn",
                "Anseong","Anseŏng","Uiwang","Yangpyeong","Yangpyeŏng","Yeoju","Yeŏju","Gwacheon","Gwacheŏn","Goyang","Gŏyang",
                "Namyangju","Paju","Uijeongbu","Uijeŏngbu","Yangju","Guri","Pocheon","Pŏcheon","Dongducheon","Dŏngducheŏn",
                "Gapyeong","Gapyeŏng","Yeoncheon","Yeŏncheŏn"};

        String[] gangwon = {"Chuncheon","Chuncheŏn","Wonju","Wŏnju","Gangneung","Donghae","Dŏnghae","Taebaek","Sokcho","Sŏkchŏ",
                "Samcheok","Samcheŏk","Hongcheon","Hŏngcheŏn","Hoengseong","Hŏengseŏng","Yeongwol","Yeŏngwŏl","Pyeongchang",
                "Pyeŏngchang","Jeongseon","Jeŏngseŏn","Cheorwon","Cheŏrwŏn","Hwacheon","Hwacheŏn","Yanggu","Inje","Goseong",
                "Gŏseŏng","Yangyang","Okcheon","Ŏkcheŏn"};

        String[] chungbuk = {"Cheongju","Cheŏngju","Chungju","Jecheon","Jecheŏn","Boeun","Bŏeun","Okcheon","Ŏkcheŏn",
                "Yeongdong","Yeŏngdŏng","Jeungpyeong","Jeungpyeŏng","Jincheon","Jincheŏn","Goesan","Gŏesan","Eumseong",
                "Eumseŏng","Danyang"};

        String[] chungnam = {"Daejeon","Daejeŏn","Sejong","Sejŏng","Cheonan","Cheŏnan","Gyeongsan","Gyeŏngsan","Gongju",
                "Gŏngju","Boryeong","Bŏryeong","Asan","Seosan","Seŏsan","Nonsan","Nŏnsan","Gyeryong","Gyeryŏng","Dangjin",
                "Geumsan","Buyeo","Buyeŏ","Seocheon","Seŏcheon","Cheongyang","Cheŏngyang","Hongseong","Hŏngseŏng","Yesan",
                "Taean"};

        String[] jeonbuk = {"Jeonju","Jeŏnju","Gunsan","Iksan","Jeongeup","Jeŏngeup","Namwon","Namwŏn","Gimje","Wanju",
                "Jinan","Muju","Jangsu","Imsil","Sunchang","Gochang","Gŏchang","Buan"};

        String[] jeonnam = {"Gwangju","Mokpo","Mŏkpŏ","Yeosu","Yeŏsu","Suncheon","Suncheŏn","Naju","Gwangyang","Damyang",
                "Gokseong","Gŏkseŏng","Gurye","Goheung","Gŏheung","Gurye","Boseong","Bŏseŏng","Hwasun","Jangheung",
                "Gangjin","Haenam","Yeongam","Yeŏngam","Muan","Hampyeong","Hampyeŏng","Yeonggwang","Yeŏnggwang",
                "Jangseong","Jangseŏng","Wando","Wandŏ","Jindo","Jindŏ","Shinan"};

        String[] gyeongbuk = {"Daegu","Pohang","Pŏhang","Gyeongju","Gyeŏngju","Gimcheon","Gimcheŏn","Andong","Andŏng",
                "Gumi","Yeongju","Yeŏngju","Yeongcheon","Yeŏngcheŏn","Sangju","Mungyeong","Mungyeŏng","Gyeongsan","Gyeŏngsan",
                "Gunwi","Uiseong","Uiseŏng","Cheongsong","Cheŏngsŏng","Yeongyang","Yeŏngyang","Yeongdeok","Yeŏngdeŏk",
                "Cheongdo","Cheŏngdŏ","Goryeong","Gŏryeŏng","Seongju","Seŏngju","Chilgok","Chilgŏk","Yecheon","Yecheŏn",
                "Bonghwa","Bŏnghwa","Uljin","Ulleung"};

        String[] gyeongnam = {"Busan","Ulsan","Changwon","Changwŏn","Gimhae","Yangsan","Jinju","Geoje","Geŏje","Tongyeong",
                "Tŏngyeŏng","Sacheon","Sacheŏn","Miryang","Haman","Changnyeong","Changnyeŏng","Geochang","Geŏchang",
                "Goseong","Gŏseong","Hadong","Hadŏng","Hapcheon","Hapcheŏn","Namhae","Hamyang","Sancheong","Sancheŏng",
                "Uiryeong","Uiryeŏng"};

        String[] jeju = {"Jeju","Seogwipo","Seŏgwipŏ"};

        String answer = null;
        for (int i = 0; i < capital.length; i++) {
            if (capital[i].equals(region)){
                answer = "수도권";
            }
        }
        for (int i = 0; i < gangwon.length; i++) {
            if (gangwon[i].equals(region)){
                answer = "강원";
            }
        }
        for (int i = 0; i < chungbuk.length; i++) {
            if (chungbuk[i].equals(region)){
                answer = "충북";
            }
        }
        for (int i = 0; i < chungnam.length; i++) {
            if (chungnam[i].equals(region)){
                answer = "충남";
            }
        }
        for (int i = 0; i < jeonbuk.length; i++) {
            if (jeonbuk[i].equals(region)){
                answer = "전북";
            }
        }
        for (int i = 0; i < jeonnam.length; i++) {
            if (jeonnam[i].equals(region)){
                answer = "전남";
            }
        }
        for (int i = 0; i < gyeongbuk.length; i++) {
            if (gyeongbuk[i].equals(region)){
                answer = "경북";
            }
        }
        for (int i = 0; i < gyeongnam.length; i++) {
            if (gyeongnam[i].equals(region)){
                answer = "경남";
            }
        }
        for (int i = 0; i < jeju.length; i++) {
            if (jeju[i].equals(region)){
                answer = "제주";
            }
        }
        return answer;

    }



}