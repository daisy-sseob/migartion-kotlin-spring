package com.group.libraryapp.domain.user

interface UserRepository {
  
  fun save(newUser: User): User
  
  fun findAll(): List<User>
  
  fun findByName(name: String): User?
  
  fun findByIdOrThrow(id: Long?): User
  
  fun delete(user: User)
  
  fun findAllWithHistory(): List<User>

}