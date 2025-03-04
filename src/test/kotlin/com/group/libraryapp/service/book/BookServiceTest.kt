package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
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
    val bookRequest = BookRequest("Effective kotlin", BookType.COMPUTER)
    
    // when
    bookService.saveBook(bookRequest)
    
    // then
    val findByName: Book = bookRepository.findByName(bookRequest.name) ?: fail()

    assertThat(findByName.name).isEqualTo(bookRequest.name)
    assertThat(findByName.type).isEqualTo(bookRequest.type)

  }

  @Test
  @DisplayName("책 대출 테스트")
  fun bookLoanTest() {
    
    // given
    val savedBook = bookRepository.save(Book.fixture())
    val savedUser = userRepository.save(User("김홍출", 30))
    val request = BookLoanRequest(savedUser.name, savedBook.name)

    // when
    bookService.loanBook(request)
    
    // then
    val findAll: MutableList<UserLoanHistory> = userLoanHistoryRepository.findAll()
    assertThat(findAll).hasSize(1)
    assertThat(findAll[0].bookName).isEqualTo(request.bookName)
    assertThat(findAll[0].user.id).isEqualTo(savedUser.id)
    assertThat(findAll[0].status).isEqualTo(UserLoanStatus.LOANED)

  }
  
  @Test
  @DisplayName("책 대출 실패 테스트")
  fun bookLoanFailTest() {

    // given
    val savedBook = bookRepository.save(Book.fixture())
    val savedUser = userRepository.save(User("김홍출", 30))
    val request = BookLoanRequest(savedUser.name, savedBook.name)
    bookService.loanBook(request)

    // when
    val anotherReq = BookLoanRequest("핑구", savedBook.name)
    
    // then
    val exception: IllegalArgumentException = assertThrows<IllegalArgumentException>({bookService.loanBook(anotherReq)})
    
    assertThat(exception.message).isEqualTo("진작 대출되어 있는 책입니다")
    
  }

  @Test
  @DisplayName("책 반납 정상 테스트")
  fun returnBookTest() {

    // given
    val savedBook = bookRepository.save(Book.fixture())
    val savedUser = userRepository.save(User("핑구", 30))
    val loanRequest = BookLoanRequest(savedUser.name, savedBook.name)
    bookService.loanBook(loanRequest)
    
    val request = BookReturnRequest(savedUser.name, savedBook.name)

    // when
    bookService.returnBook(request)
    
    // then
    val findAll = userLoanHistoryRepository.findAll()
    assertThat(findAll).hasSize(1)
    assertThat(findAll[0].status).isEqualTo(UserLoanStatus.RETURNED)

  }

  @Test
  @DisplayName("책 대여 권수를 정상 확인 테스트")
  fun countLoanedBookTest() {
    
    // given
    val savedUser = userRepository.save(User("핑구", null))
    userLoanHistoryRepository.saveAll(
      listOf(
        UserLoanHistory.fixture(savedUser, "java"),
        UserLoanHistory.fixture(savedUser, "kotlin", UserLoanStatus.RETURNED),
        UserLoanHistory.fixture(savedUser, "Effective Java", UserLoanStatus.RETURNED),
      )
    )
    
    // when
    val result = bookService.countLoanedBook()
    
    // then
    assertThat(result).isEqualTo(1)
    
  }

  @Test
  @DisplayName("분야별 책 권수 정상 확인 테스트 ")
  fun getBookStatisticsTest() {
    
    // given
    bookRepository.saveAll(
      listOf(
        Book.fixture("Java", BookType.COMPUTER),
        Book.fixture("Kotlin", BookType.COMPUTER),
        Book.fixture("Effective Java", BookType.COMPUTER),
        Book.fixture("Earth", BookType.SCIENCE),
      )
    )
    
    // when
    val results = bookService.getBookStatistics()

    // then
    assertCount(results, BookType.COMPUTER, 3)
    assertCount(results, BookType.SCIENCE, 1)
    
  }

  private fun assertCount(results: List<BookStatResponse>, type: BookType, count: Int) {
    assertThat(results.first {result -> result.type == type}.count).isEqualTo(count)
  }
  
}