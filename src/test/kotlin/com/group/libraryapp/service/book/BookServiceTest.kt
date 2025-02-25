package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.util.fail
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
  private val bookService: BookService,
  private val bookRepository: BookRepository,
  private val userRepository: UserRepository,
  private val userLoanHistoryRepository: UserLoanHistoryRepository,) {

  @AfterEach
  fun clear() {
    bookRepository.deleteAll()
    userRepository.deleteAll()
  }

  @Test
  @DisplayName("책 등록이 정상 동작 테스트")
  fun saveBookTest() {
    
    // given
    val bookRequest = BookRequest("Effective kotlin")
    
    // when
    bookService.saveBook(bookRequest)
    
    // then
    val findByName: Book = bookRepository.findByName(bookRequest.name) ?: fail()

    assertThat(findByName.name).isEqualTo(bookRequest.name)

  }

  @Test
  @DisplayName("책 대출 테스트")
  fun bookLoanTest() {
    
    // given
    bookRepository.save(Book("Effective Kotlin"))
    val savedUser = userRepository.save(User("김홍출", 30))
    val request = BookLoanRequest("김홍출", "Effective Kotlin")

    // when
    bookService.loanBook(request)
    
    // then
    val findAll: MutableList<UserLoanHistory> = userLoanHistoryRepository.findAll()
    assertThat(findAll).hasSize(1)
    assertThat(findAll[0].bookName).isEqualTo(request.bookName)
    assertThat(findAll[0].user.id).isEqualTo(savedUser.id)
    assertThat(findAll[0].isReturn).isFalse()

  }
  
  @Test
  @DisplayName("책 대출 실패 테스트")
  fun bookLoanFailTest() {

    // given
    bookRepository.save(Book("Effective Kotlin"))
    userRepository.save(User("김홍출", 30))
    val request = BookLoanRequest("김홍출", "Effective Kotlin")
    bookService.loanBook(request)

    // when
    val anotherReq = BookLoanRequest("핑구", "Effective Kotlin")
    
    // then
    val exception: IllegalArgumentException = assertThrows<IllegalArgumentException>({bookService.loanBook(anotherReq)})
    
    assertThat(exception.message).isEqualTo("진작 대출되어 있는 책입니다")
    
  }

  @Test
  @DisplayName("책 반납 정상 테스트")
  fun returnBookTest() {

    // given
    bookRepository.save(Book("Effective Kotlin"))
    val savedUser = userRepository.save(User("핑구", 30))
    val loanRequest = BookLoanRequest("핑구", "Effective Kotlin")
    bookService.loanBook(loanRequest)
    
    val request = BookReturnRequest("핑구", "Effective Kotlin")

    // when
    bookService.returnBook(request)
    
    // then
    val findAll = userLoanHistoryRepository.findAll()
    assertThat(findAll).hasSize(1)
    assertThat(findAll[0].isReturn).isTrue()

  }
}