package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service    // 서비스 객체 생성
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;    //  게시글 레포지토리 객체 주입

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();       // dto -> 엔티티로 변환후 article에 저장
        if(article.getId() != null){
            return null;
        }
        return articleRepository.save(article); // article을 DB에 저장
    }

    public Article update(Long id, ArticleForm dto) {
        Article article = dto.toEntity();   // 1. dto를 엔티티로 변환
        log.info("id: {}, article: {}", id, article.toString());    // 2. 로그 찍기
        // 2. 타겟 조회
        Article target = articleRepository.findById(id).orElse(null);
        // 3. 잘못된 요청 처리
        if (target == null || id != article.getId()) {        // 1. 잘못된 요청인지 판별
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());   // 2. 로그 찍기
            return null;        //응답은 컨트롤러가 하므로 null 반환
        }        // 4. 업데이트 및 정상 응답(200) 하기
            target.patch(article);                          // 1. 기존 데이터에 새 데이터 붙이기
            Article updated = articleRepository.save(target);   // 2. 수정 내용 DB에 최종 저장
            return updated;     //응답은 컨트롤러가 하므로 여기서는 수정 데이터만 반환
    }

    public Article delete(Long id) {
        // 1. 대상 찾기
        Article target = articleRepository.findById(id).orElse(null);
        // 2. 잘못된 요청 처리하기
        if(target == null){
            return null;        // 응답은 컨트롤러가 하므로 null 반환
        }                   //잘못된 요청이 아니라면 대상 엔티티를 삭제
        // 3. 대상 삭제하기
        articleRepository.delete(target);
        return target;          // DB에서 삭제한 대상을 컨트롤러에 반환
    }
@Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {
        // 1. dtos 묶음을 엔티티 묶음으로 변환
        /*  1) dtos를 스트림화
            2) map()으로 dto가 하나하나 올 때마다 dto.toEntity()를 수행해 매핑
            3) 매핑한 것을 리스트로 묶음
            4) 최종 결과를 articleList에 저장*/
        // List<Article> articleList = new ArrayList<>();
        // for (int i = 0; i < dtos.size(); i++){
        // ArticleForm dto = dtos.get(i);
        // Article entity = dto.toEntity();
        // articleList.add(entity);
        // }
        List<Article> articleList = dtos.stream()   //1. 4.
                .map(dto -> dto.toEntity())         //2.
                .collect(Collectors.toList());      //3.
        // 2. 엔티티 묶음을 DB에 저장
//        for (int i = 0; i < articleList.size(); i++){
//            Article article = articleList.get(i);
//            articleRepository.save(article);
//        }
        articleList.stream()
                .forEach(article -> articleRepository.save(article));
        // 3. 강제로 에러 발생
        articleRepository.findById(-1L)     //id 가 -l인 데이터 찾기
                .orElseThrow(() -> new IllegalArgumentException("결제 실패!")); // 찾는 데이터가 없으면 예외 발생
        // 4. 결과 값 반환
        return articleList;
    }
}

