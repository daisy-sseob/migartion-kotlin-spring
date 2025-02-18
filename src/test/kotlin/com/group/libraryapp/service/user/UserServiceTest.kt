package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
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

  private val userRepository: UserRepository,
  private val userService: UserService,

  ) {

  // 각 테스트에 대한 DB 자원 영향을 받지 않게 하기 위해서.
  @AfterEach
  fun clean() {
    userRepository.deleteAll()
  }

  @Test
  @DisplayName("유저 저장 정상 테스트")
  fun saveUserTest() {
    //  given
    val req = UserCreateRequest("A", null)

    // when
    userService.saveUser(req)

    // then
    val users: List<User> = userRepository.findAll()

    assertThat(users).hasSize(1)
    assertThat(users[0].name).isEqualTo("A")
    assertThat(users[0].age).isNull()

  }

  @Test
  @DisplayName("유저 조회 정상 테스트")
  fun getUsersTest() {

    // given
    userRepository.saveAll(
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
    val user = userRepository.save(User("A", 10))
    val request = UserUpdateRequest(user.id, "B")

    // when
    userService.updateUserName(request)

    // then
    val findUser: Optional<User> = userRepository.findById(user.id)

    assertThat(findUser.isPresent).isTrue
    assertThat(findUser.get().name).isEqualTo("B")

  }

  @Test
  fun deleteUserTest() {
    
    // given
    val user = userRepository.save(User("A", 10))
    
    // when
    userService.deleteUser("A")
    
    // then
    val findAll = userRepository.findAll()

    assertThat(findAll).isEmpty()


  }
  
}
