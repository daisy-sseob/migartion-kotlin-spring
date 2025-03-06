package com.group.libraryapp.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class QueryDslConfig(
  private val entityManager: EntityManager
) {

  @Bean
  fun queryDsl(): JPAQueryFactory {
    return JPAQueryFactory(entityManager)
  }
  
}