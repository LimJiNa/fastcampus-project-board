package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // 기존적으로 Entity에 있는 모든 필드에 대한 기본 검색 기능을 추가해준다
        QuerydslBinderCustomizer<QArticle>
{
    /**
     * 여기 안에 구현된 내용을 토대로 검색에 대한 세부적인 규칙이 재구성된다.
     *
     * 레포지토리 레이어에서 직접 구현체를 만들지 않을거고
     * spring data jpa를 이용해서 인터페이스만 가지고 기능을 다 사용하게끔 접근하고 있어서
     * default 메소드를 사용하는 것이 적절해 보이고 여기서 커스터마이즈 구현을 여기서 들어간다.
     */
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        /**
         * 선택적으로 검색이 가능하도록 설정
         */
        bindings.excludeUnlistedProperties(true);

        /**
         * 원하는 필드 추가
         */
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);

        /**
         * 검색 파라미터 하나만 받는다.
         * IgnoreCase를 사용하여 대소문자 구분하지 않게 설정
         */
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // 생성쿼리 - like '%${value}%'
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // 생성쿼리 - like '${value}'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
