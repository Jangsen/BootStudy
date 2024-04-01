package com.example.firstproject.repository;
//CrudRepository 패키지 자동 임포트
import com.example.firstproject.entity.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long>{

}
