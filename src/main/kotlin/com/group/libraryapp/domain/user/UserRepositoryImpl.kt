package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.user.QUser.user
import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory.userLoanHistory
import com.group.libraryapp.util.findByIdOrThrow
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
  private val userJpaRepository: UserJpaRepository,
  private val queryDsl: JPAQueryFactory,
) : UserRepository {
  
  override fun findByName(name: String): User? {
    return userJpaRepository.findByName(name)
  }

  override fun save(newUser: User): User {
    return userJpaRepository.save(newUser)
  }

  override fun findAll(): List<User> {
    return userJpaRepository.findAll()
  }

  override fun findByIdOrThrow(id: Long?): User {
    return userJpaRepository.findByIdOrThrow(id)
  }

  override fun delete(user: User) {
    userJpaRepository.delete(user)
  }

  override fun findAllWithHistory(): List<User> {
    return queryDsl
      .select(user).distinct()
      .from(user)
      .leftJoin(userLoanHistory).on(userLoanHistory.user.id.eq(user.id)).fetchJoin()
      .fetch()
  }
  
  
}