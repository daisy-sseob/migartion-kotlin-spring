package com.group.libraryapp.domain.book

import javax.persistence.*


/**
 * 
 * JPA entity는 기본 생성자가 있어야 등록된다. <br>
 * 
 * 때문에 org.jetbrains.kotlin.plugins.jpa 플러그인이 필요하다.
 * 
 */
@Entity
class Book(

  val name: String,

  @Enumerated(EnumType.STRING)
  val type: BookType,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  ) {

  /* 생성자 호출시 init block 이 호출된다. */
  init {
    if (name.isBlank()) {
      throw IllegalArgumentException("이름은 비어 있을 수 없습니다.")
    }
  }

  companion object {
    
    // test code 만을 위한 코드. 
    fun fixture(name: String = "책 이름", type: BookType = BookType.COMPUTER, id: Long? = null): Book {
      return Book(name, type, id)
    }
  }

}