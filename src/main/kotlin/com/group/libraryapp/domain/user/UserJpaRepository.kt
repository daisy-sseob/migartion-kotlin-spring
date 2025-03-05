package com.group.libraryapp.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, Long> {

  fun findByName(name: String): User?
  
}