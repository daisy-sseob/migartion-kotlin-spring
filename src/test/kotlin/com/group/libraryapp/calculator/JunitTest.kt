package com.group.libraryapp.calculator

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JunitTest {

  companion object {
    
    @JvmStatic
    @BeforeAll
    fun beforeAll() {
      println("모든 테스트 시작전")
    }
    
    @JvmStatic
    @AfterAll
    fun afterAll() {
      println("모든 테스트 종료 후")
    }
  }
  

  @BeforeEach
  fun beforeEach() {
    println("before each")
    println()
  }

  @AfterEach
  fun afterEach() {
    println("after each")
    println()
  }

  @Test
  fun test1() {
    println("test 1")
  }
  
  @Test
  fun test2() {
    println("test 2")
  }
  
}