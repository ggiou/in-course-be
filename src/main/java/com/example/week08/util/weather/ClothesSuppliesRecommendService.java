package com.example.week08.util.weather;

import com.example.week08.domain.ClothesSupplies;
import com.example.week08.domain.Member;
import com.example.week08.domain.OpenWeatherData;
import com.example.week08.dto.response.ClothesSuppliesResponseDto;
import com.example.week08.errorhandler.BusinessException;
import com.example.week08.errorhandler.ErrorCode;
import com.example.week08.jwt.TokenProvider;
import com.example.week08.repository.ClothesSuppliesDataRepository;
import com.example.week08.repository.OpenWeatherDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

//w의 경우 여자, m의 경우 남자
@Service
@RequiredArgsConstructor
public class ClothesSuppliesRecommendService {


    private final String[]sfHat={"없음", "없음", "없음", "없음","벙거지 모자", "베레모", "볼 캡" }; //봄, 가을
    private final String[]summerHat={"없음", "없음", "없음", "없음","볼 캡", "밀짚 모자_w"};//여름
    private final String[]winterHat={"없음", "없음", "없음", "없음","비니", "볼 캡", "방울 모자"};//겨울

    //계절별 모자

    private final String[]offishOuterwear={"없음","얇은 가디건", "얇은 재킷", "트렌치 코트", "블레이저",
    "바람 막이", "야상 자켓", "항공 점퍼", "얇은 코트"};
    private final String[]coldOuterwear={"롱 패딩", "숏 패딩", "롱 코트", "무스탕",
    "보머 패딩(항공 패딩)", "야상 패딩", "퍼 코트_w", "다운 점퍼", "가죽 자켓"};
    //겉 옷

    private final String[]oneTop={"기모 후드티","기모 니트", "기모 맨투맨", "스웨터", "기모 원피스_w" }; //5
    private final String[]twoTop={"기모 후드티","기모 니트", "기모 맨투맨","기모 원피스_w"};        //6~9
    private final String[]threeTop={"기모 후드티","기모 니트", "기모 맨투맨", "블라우스_w"};      //10~11
    private final String[]fourTop={"후드티", "니트", "맨투맨", "블라우스_w", "원피스_w"};       //12~16
    private final String[]fiveTop={"얇은 니트", "얇은 후드티", "얇은 맨투맨", "블라우스_w", "원피스_w"};       //17~19
    private final String[]sixTop={"얇은 긴팔", "얇은 후드티", "블라우스_w", "원피스_w"};        //20~22
    private final String[]sevenTop={"반팔 티셔츠", "얇은 셔츠", "얇은 긴팔", "반팔 블라우스_w", "오픈 숄더 블라우스_w", "얇은 원피스_w"};      //23~26
    private final String[]eightTop={"민소매", "얇은 원피스_w", "반팔 티셔츠", "반팔 블라우스_w", "오픈 숄더 블라우스_w", "민소매 원피스_w"};      //27
    //계절별 상의

    private final String[]onePants={"울 스커트_w", "롱 스커트_w", "니트 스커트_w", "기모 조커 팬츠",
    "기모 팬츠", "기모 와이드 슬랙스", "데님 팬츠", "기모 청바지", "기모 정장 바지"}; //5
    private final String[]twoPants={"울 스커트_w", "롱 스커트_w", "니트 스커트_w", "기모 조커 팬츠",
            "기모 팬츠", "기모 와이드 슬랙스", "데님 팬츠", "기모 청바지", "기모 정장 바지"};        //6~9
    private final String[]threePants={"울 스커트_w", "롱 스커트_w", "니트 스커트_w", "기모 조커 팬츠",
            "기모 팬츠", "기모 와이드 슬랙스", "데님 팬츠", "기모 청바지", "기모 정장 바지"};      //10~11
    private final String[]fourPants={"플리츠 스커트_w", "머메이드 스커트_w", "롱 스커트_w", "미니 스커트_w", "레더 스커트_w"
    , "골지 니트 스커트_w","슬랙스", "카고 팬츠", "청바지", "정장 바지", "배기 팬츠"};       //12~16
    private final String[]fivePants={"플리츠 스커트_w", "머메이드 스커트_w", "롱 스커트_w", "미니 스커트_w", "레더 스커트_w"
    ,"슬랙스", "카고 팬츠", "청바지", "정장 바지", "배기 팬츠"};       //17~19
    private final String[]sixPants={"플리츠 스커트_w", "미니 스커트_w", "레더 스커트_w",
        "면 바지","슬랙스", "카고 팬츠", "청바지", "정장 바지", "배기 팬츠"};        //20~22
    private final String[]sevenPants={"반 바지", "숏 팬츠_w", "미니 스커트_w", "카고 바지", "청바지", "정장 바지"
            ,"여름 슬랙스","린넨 반바지", "쿨링 팬츠", "데님 스커트_w", "트임 스커트_w"};      //23~26
    private final String[]eightPants={"반 바지", "숏 팬츠_w", "미니 스커트_w", "카고 바지", "청바지", "정장 바지"
    ,"여름 슬랙스", "린넨 반바지", "쿨링 팬츠", "데님 스커트_w", "트임 스커트_w"};      //27
    //계절별 하의

    private final String[]snowShoes={"털 부츠_w", "방수 운동화" };       //눈 올때 신발
    private final String[]rainShoes={"방수 운동화", "단화", "레인부츠"};        //비 올때 신발
    private final String[]sfShoes={"운동화", "구두", "하이힐_w", "단화"};      //봄, 가을
    private final String[]summerShoes={"슬리퍼", "여름 운동화", "구두", "여름 단화"};      //여름
    private final String[]winterShoes={"롱 부츠_w", "구두", "운동화", "겨울 워커"};      //겨울
    //계절별 신발

    private final String[]sfSupplies={"마스크", "담요", "물통"}; // 12~22, 봄&가을
    private final String[]summerSupplies={"선크림", "선글라스", "물통", "양산"}; //27~23
    private final String[]winterSupplies={"목도리", "핫팩", "장갑", "히트 택","내복", "보온병", "귀도리"}; //5~11
    //계절별 챙길 것(택 2)

    private final String[]rainSupplies={"우산", "우비"};
    private final String[]snowSupplies={"손난로", "우산"};

    private final OpenWeatherDataRepository openWeatherDataRepository;
    private final ClothesSuppliesDataRepository clothesSuppliesDataRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ClothesSuppliesResponseDto clothesSuppliesRecommend(HttpServletRequest request){
        Member member = validateMember(request);
        if (null == member) {
            throw new BusinessException("회원만 사용가능한 서비스 입니다.", ErrorCode.JWT_NOT_PERMIT);
        }

        Optional<OpenWeatherData> optionalWeather = openWeatherDataRepository.findByMember(member);
        OpenWeatherData weatherData = optionalWeather.get();

        String hat;
        String top;
        String outerwear;
        String pants;
        String shoes = null;
        String supplies = null;
        String supplies2;




        if (weatherData.getTemp()<=5){
            top = getTop(member, oneTop);
        } else if (weatherData.getTemp()>5&&weatherData.getTemp()<=9) {
            top = getTop(member, twoTop);
        } else if (weatherData.getTemp()>9&&weatherData.getTemp()<=11) {
            top = getTop(member, threeTop);
        }else if (weatherData.getTemp()>11&&weatherData.getTemp()<=16) {
            top = getTop(member, fourTop);
        }else if (weatherData.getTemp()>16&&weatherData.getTemp()<=19) {
            top = getTop(member, fiveTop);
        }else if (weatherData.getTemp()>19&&weatherData.getTemp()<=22) {
            top = getTop(member, sixTop);
        }else if (weatherData.getTemp()>22&&weatherData.getTemp()<=26) {
            top = getTop(member, sevenTop);
        }else {
            top = getTop(member, eightTop);
        }
        // 온도 별 상의
        System.out.println(top+"\n");

        if (weatherData.getTemp()>23){
            hat = getHat(member, summerHat, top);
        }else if (weatherData.getTemp()<=11){
            hat = getHat(member, winterHat, top);
        }else {
            hat = getHat(member, sfHat, top);
        }
        //온도별 모자
        System.out.println(hat+"\n");


        if (weatherData.getTemp()<=14){
            outerwear = getTop(member, offishOuterwear);
        } else if (weatherData.getTemp()<=8) {
            outerwear = getTop(member, coldOuterwear);
        } else {
            outerwear = "없음";
        }
        //온도별 겉옷
        System.out.println(outerwear+"\n");


        if (weatherData.getTemp()<=5){
            pants = getPants(member, onePants, top, weatherData);
        } else if (weatherData.getTemp()>5&&weatherData.getTemp()<=9) {
            pants = getPants(member, twoPants, top, weatherData);
        } else if (weatherData.getTemp()>9&&weatherData.getTemp()<=11) {
            pants = getPants(member, threePants, top, weatherData);
        }else if (weatherData.getTemp()>11&&weatherData.getTemp()<=16) {
            pants = getPants(member, fourPants, top, weatherData);
        }else if (weatherData.getTemp()>16&&weatherData.getTemp()<=19) {
            pants = getPants(member, fivePants, top, weatherData);
        }else if (weatherData.getTemp()>19&&weatherData.getTemp()<=22) {
            pants = getPants(member, sixPants, top, weatherData);
        }else if (weatherData.getTemp()>22&&weatherData.getTemp()<=26) {
            pants = getPants(member, sevenPants, top, weatherData);
        }else {
            pants = getPants(member, eightPants, top, weatherData);
        }
        // 온도 별 하의
        System.out.println(pants+"\n");

        if (weatherData.getWeather().contains("비")){
            shoes = getShoes(member, rainShoes);
        } else if (weatherData.getWeather().contains("눈")) {
            shoes = getShoes(member, snowShoes);
        }
        if(!weatherData.getWeather().contains("비")||!weatherData.getWeather().contains("눈")){
            if(weatherData.getTemp()>=23){
                shoes = getShoes(member,summerShoes);
            } else if (weatherData.getTemp()<=11) {
                shoes = getShoes(member, winterShoes);
            }else {
                shoes = getShoes(member, sfShoes);
            }
        }
        System.out.println(shoes+"\n");

        //계절 별 신발

        if(weatherData.getWeather().contains("비")){
            supplies = getString(rainSupplies);
        } else if (weatherData.getWeather().contains("눈")) {
            supplies = getString(snowSupplies);
        }else {
            supplies ="없음";
        }
        if(weatherData.getTemp()>=23){
            supplies2 = getString(summerSupplies);
            if(supplies.contains("없음")){
                do {
                    supplies = getString(summerSupplies);
                } while (supplies.equals(supplies2));
            }
        } else if (weatherData.getTemp()<=11) {
            supplies2 = getString(winterSupplies);
            if (supplies.contains("없음")){
                do {
                    supplies = getString(winterSupplies);
                } while (supplies.equals(supplies2));
            }

        }else {
            supplies2 = getString(sfSupplies);
            if (supplies.contains("없음")){
                do {
                    supplies = getString(sfSupplies);
                } while (supplies.equals(supplies2));
            }
        }
        // 비, 눈이거나 온도에 대한 챙길 것 1, 2번 추천

        if(clothesSuppliesDataRepository.findByMember(member).isEmpty()){
            ClothesSupplies newData = ClothesSupplies.builder()
                    .member(member)
                    .hat(hat)
                    .top(top)
                    .outerwear(outerwear)
                    .pants(pants)
                    .shoes(shoes)
                    .supplies(supplies)
                    .supplies2(supplies2)
                    .build();
            clothesSuppliesDataRepository.save(newData);
        }else {
            ClothesSupplies dt = isPresent(member);
            dt.update(hat, top, outerwear, pants, shoes, supplies, supplies2);
            clothesSuppliesDataRepository.save(dt);
        }

        Optional<ClothesSupplies> d = clothesSuppliesDataRepository.findByMember(member);
        ClothesSupplies data = d.get();
        System.out.println(data.getMember().getNickname()+"\n");
        return new ClothesSuppliesResponseDto(data);
    }

    private String getHat(Member member, String[] x, String top) {
        String hat;
        System.out.println(member.getGender()+"/n");
        if(member.getGender().contains("남성")){
            do {
                hat = getString(x);
            } while (hat.contains("_w"));
        } // 남성의 경우 여성의류 추천 x
        else {
            hat = getString(x);
        }
        if (hat.contains("_w")){
            int idx = hat.indexOf("_");
            String ht;
            ht = hat.substring(0, idx);
            return ht;
        } //뒤에 _w 때서 저장
        if (top.contains("원피스")){
            hat = "없음";
        }
        return hat;
    } //상의 추천
    private String getTop(Member member, String[] x) {
        String top;
        if(member.getGender().contains("남성")){
            do {
                top = getString(x);
            } while (top.contains("_w"));
        } // 남성의 경우 여성의류 추천 x
        else {
            top = getString(x);
        }
        if (top.contains("_w")){
            int idx = top.indexOf("_");
            String tp;
            tp = top.substring(0, idx);
            return tp;
        } //뒤에 _w 때서 저장
        return top;
    } //상의 추천

    private String getPants(Member member, String[] x, String top, OpenWeatherData weatherData) {
        String pants;
        if(member.getGender().contains("남성")){
            do {
                pants = getString(x);
            } while (pants.contains("_w"));
        } // 남성의 경우 여성의류 추천 x
        else{
            pants = getString(x);
        }
        if (pants.contains("_w")){
            int idx = pants.indexOf("_");
            String pt;
            pt = pants.substring(0, idx);
            return pt;
        } //뒤에 _w 때서 저장
        if (top.contains("원피스")){
            if(weatherData.getTemp()<=11) {
                pants = "기모 스타킹";
            } else if (weatherData.getTemp()>=23) {
                pants = "없음";
            } else {
                pants = "스타킹";
            }
        } //원피스의 경우 하의 스타킹 대체
        return pants;
    } //하의 추천

    private String getShoes(Member member, String[] x) {
        String shoes;
        if(member.getGender().contains("남성")){
            do {
                shoes = getString(x);
            } while (shoes.contains("_w"));
        } // 남성의 경우 여성 신발 추천 x
        else{
            shoes = getString(x);
        }
        if (shoes.contains("_w")){
            int idx = shoes.indexOf("_");
            String so;
            so = shoes.substring(0, idx);
            return so;
        } //뒤에 _w 때서 저장
        return shoes;
    } //신발 추천
    private String getString(String[] x ) {
        double random = Math.random();
        String a;
        int num = (int)Math.round(random*(x.length-1));
        a = x[num];
        return a;
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
    @Transactional(readOnly = true)
    public ClothesSupplies isPresent(Member member) {
        Optional<ClothesSupplies> optionalData = clothesSuppliesDataRepository.findByMember(member);
        return optionalData.orElse(null);
    }
}
