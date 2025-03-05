package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserJpaRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class UserServiceTest @Autowired constructor(

  private val userJpaRepository: UserJpaRepository,
  private val userService: UserService,
  private val userLoanHistoryRepository: UserLoanHistoryRepository,

  ) {

  // 각 테스트에 대한 DB 자원 영향을 받지 않게 하기 위해서.
  @AfterEach
  fun clean() {
    print("=========== clean ===========")
    userJpaRepository.deleteAll()
  }

  @Test
  @DisplayName("유저 저장 정상 테스트")
  fun saveUserTest() {
    //  given
    val req = UserCreateRequest("A", null)

    // when
    userService.saveUser(req)

    // then
    val users: List<User> = userJpaRepository.findAll()

    assertThat(users).hasSize(1)
    assertThat(users[0].name).isEqualTo("A")
    assertThat(users[0].age).isNull()

  }

  @Test
  @DisplayName("유저 조회 정상 테스트")
  fun getUsersTest() {

    // given
    userJpaRepository.saveAll(
      listOf(
        User("A", 10),
        User("B", 16),
        User("C", null),
        User("D", null),
        User("E", 30),
      )
    )

    // when
    val users = userService.getUsers()

    // then
    assertThat(users).hasSize(5)
    assertThat(users).extracting("name").containsExactlyInAnyOrder("A", "B", "C", "D", "E")
    assertThat(users).extracting("age").containsExactlyInAnyOrder(10, 16, null, null, 30)

  }

  @Test
  @DisplayName("유저 이름 수정 정상 테스트")
  fun updateUserNameTest() {

    // given
    val user = userJpaRepository.save(User("A", 10))
    val request = UserUpdateRequest(user.id, "B")

    // when
    userService.updateUserName(request)

    // then
    val findUser: Optional<User> = userJpaRepository.findById(user.id)

    assertThat(findUser.isPresent).isTrue
    assertThat(findUser.get().name).isEqualTo("B")

  }

  @Test
  fun deleteUserTest() {
    
    // given
    val user = userJpaRepository.save(User("A", 10))
    
    // when
    userService.deleteUser("A")
    
    // then
    val findAll = userJpaRepository.findAll()

    assertThat(findAll).isEmpty()


  }

  @Test
  @DisplayName("대출 기록이 없는 유저도 응답에 포함된다.")
  fun getUserLoanHistoriesTest() {
    // given
    userJpaRepository.save(User("user", null))
    
    // when
    val results = userService.getUserLoanHistories()

    // then
    assertThat(results).hasSize(1)
    assertThat(results[0].name).isEqualTo("user")
    assertThat(results[0].books).isEmpty()
    
  }
  
  @Test
  @DisplayName("대출 기록이 없는 유저도 응답에 포함된다.")
  fun getUserLoanHistoriesTest2() {
    // given
    val savedUser: User = userJpaRepository.save(User("user", null))
    
    userLoanHistoryRepository.saveAll(listOf(
      UserLoanHistory.fixture(savedUser, "book1", UserLoanStatus.LOANED),
      UserLoanHistory.fixture(savedUser, "book2", UserLoanStatus.LOANED),
      UserLoanHistory.fixture(savedUser, "book3", UserLoanStatus.RETURNED),
    ))

    // when
    val results = userService.getUserLoanHistories()
 
    // then
    assertThat(results).hasSize(1)
    assertThat(results[0].name).isEqualTo("user")
    assertThat(results[0].books).isNotEmpty()
    assertThat(results[0].books).hasSize(3)
    assertThat(results[0].books).extracting("name").containsExactlyInAnyOrder("book1", "book2", "book3")
    assertThat(results[0].books).extracting("isReturn").containsExactlyInAnyOrder(false, false, true)
    
  }
  
}
