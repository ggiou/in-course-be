package com.example.week08.util;

import com.example.week08.domain.Post;
import com.example.week08.repository.PostRepository;
import com.example.week08.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor// final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {
    private final PostRepository postRepository;


    // 초, 분, 시, 일, 월, 주 순서
    //매일 AM 01:00 마다
    // 게시물에 새로운 게시물 컬럼을 만들고, 새로생성되면 true, 생성된지 일주일이 지나면 false로 바뀌게 만듬
    // 게시물의 newpost값이 true인 값만
    @Scheduled(cron = " 0 0 1 * * *")
    public void updatePostByNewPost() {
        log.info("게시글 업데이트 실행");
        //1. 게시글이 저장될 때 시간을 가져온다(타임스탬프) 2022-09-16T15:13:10.972553
        //2. 현재시간을 가져온다.
        //3. 비교한다
        //4. 일주일이 지났으면 newpost를 false로 바꾼다
        List<Post> posts = postRepository.findByNewPost(false);
        ArrayList<LocalDate> postArrayList = new ArrayList<>();
        for (Post post : posts) {
            //받아온 생성시간의 년월일 부분만 리스트에 넣는다.
            LocalDate local = post.getCreatedAt().toLocalDate();
            LocalDate sevendays = local.plusDays(7);
            LocalDate now = LocalDate.now();
            postArrayList.add(sevendays);
            if (sevendays.isAfter(now)||(sevendays.isEqual(now))) {
                post.updatePostByNewPost(false);
            }
        }
        }
    }

