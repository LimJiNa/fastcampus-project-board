package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment>
{
    /**
     * 여기 안에 구현된 내용을 토대로 검색에 대한 세부적인 규칙이 재구성된다.
     *
     * 레포지토리 레이어에서 직접 구현체를 만들지 않을거고
     * spring data jpa를 이용해서 인터페이스만 가지고 기능을 다 사용하게끔 접근하고 있어서
     * default 메소드를 사용하는 것이 적절해 보이고 여기서 커스터마이즈 구현을 여기서 들어간다.
     */
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        /**
         * 선택적으로 검색이 가능하도록 설정
         */
        bindings.excludeUnlistedProperties(true);

        /**
         * 원하는 필드 추가
         */
        bindings.including(root.content, root.createdAt, root.createdBy);

        /**
         * 검색 파라미터 하나만 받는다.
         * IgnoreCase를 사용하여 대소문자 구분하지 않게 설정
         */
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
