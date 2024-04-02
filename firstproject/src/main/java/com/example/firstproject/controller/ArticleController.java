package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ArticleController {
    @Autowired      //스프링 부트가 미리 생성해 놓은 레포지토리 객제 주입(DI)     ** 의존성 주입 **
private ArticleRepository articleRepository;
    @GetMapping("/articles/new")
        public String newArticleForm(){
            return "articles/new";
        }
    @PostMapping("/articles/create")
        public String createArticle(ArticleForm form){

            log.info(form.toString());              // 로깅 코드 추가
//            System.out.println(form.toString());     기존 println()문 주석처리
            // 1. DTO를 엔티티로 변환
            Article article = form.toEntity();
            log.info(article.toString());
//            System.out.println(article.toString());
            // 2. 레포지토리로 엔티티를 DB에 저장
            Article saved = articleRepository.save(article);
            log.info(saved.toString());
//            System.out.println(saved.toString());
            return "";
        }
        @GetMapping("/articles/{id}")       // 데이터 조회 요청 접수
        public String show(@PathVariable Long id, Model model){      // 매개변수로 id 받아오기        모델을 사용하기 위해  model 객체 받아오기
            log.info("id =" + id);
            // 1. id를 조회해 데이터 가져오기
//            Article articleEntity = articleRepository.findById(id);   빨간 물결 표시  articleRepository가 findBy(id)로 찾은 값을 반환 할 때 반환형이 Article이 아니기때문에 발생하는 문제
//            Optional<Article> aricleEntity = articleRepository.findById(id);   반환형 == Optional<Article> 타입    자바 8 이상 사용 가능
              Article articleEntity = articleRepository.findById(id).orElse(null);  // id 값이 없으면 null을 반환하라는 뜻 ( 값이 있으면 articleEntity 변수에 값을 넣고 없으면 null을 저장 )
            // 2. 모델에 데이터 등록하기  (articleEntity에 담긴 데이터를 모델에 등록하는 이유 MVC 패턴에 따라 조회한 데이터를 뷰 페이지에서 사용하기 위함)
              //name 이라는 이름으로 value 객체 추가
              //model.addAttribute(String name, Object value);
              model.addAttribute("article", articleEntity);
            // 3. 뷰 페이지 반환하기
              return "articles/show";
        }
        @GetMapping("/articles")
        public String index(Model model){       // 모델 객체 받아오기
            // 1. 모든 데이터 가져오기
            ArrayList<Article> articleEntityList = articleRepository.findAll();    // findAll() 메소드가 반환하는 데이터 타입은 Iterable인데 작성한 타입은 List
            // ArrayList의 상위 타입인 List로도 업캐스팅 가능
            // 해결방법 3가지 (메모장 참고)
            model.addAttribute("articleList", articleEntityList);   // articleEntityList 등록
            // 2. 모델에 데이터 등록하기
            // 3. 사용자에게 보여 줄 뷰 페이지 설정하기
            return "articles/index";
        }
}

// println()을 사용하지 않으려면 로깅을 해야함.
// @Slf4j   ==    Simple Logging Facade for Java  ==   로깅 기능 사용 가능
// println()문으로 확인했을 때는 뒷부분만 출력하지만
// 리팩터링 한 후에는 ArticleController에서 어떤 데이터가 언제 저장됐는지 시간 정보도 남아 있음
