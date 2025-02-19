package com.group.libraryapp.domain.book

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


/*
* JPA entity는 기본 생성자가 있어야 등록된다.
* 
* 때문에 org.jetbrains.kotlin.plugins.jpa 플러그인이 필요하다.
* 
* */

@Entity
class Book(
  val name: String,
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
) {
}