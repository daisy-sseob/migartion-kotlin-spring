package com.group.libraryapp.calculator

fun main() {

  val calculatorTest = CalculatorTest()
  calculatorTest.addTest()
  calculatorTest.minusTest()
  calculatorTest.multiplyTest()
  calculatorTest.divideTest()
  calculatorTest.divideZeroTest()

}

class CalculatorTest {

  fun addTest() {

    // given
    val calculator = Calculator(5)

    // when
    calculator.add(3)

    // then
    if (calculator.number != 8) {
      throw IllegalArgumentException()
    }

  }

  fun minusTest() {

    // given
    val calculator = Calculator(5)

    // when
    calculator.minus(3)

    // then
    if (calculator.number != 2) {
      throw IllegalArgumentException()
    }

  }
  
  fun multiplyTest() {

    // given
    val calculator = Calculator(5)

    // when
    calculator.multiply(3)

    // then
    if (calculator.number != 15) {
      throw IllegalArgumentException()
    }

  }
  
  fun divideTest() {

    // given
    val calculator = Calculator(5)

    // when
    calculator.divide(1)

    // then
    if (calculator.number != 5) {
      throw IllegalArgumentException()
    }

  }
  
  fun divideZeroTest() {

    // given
    val calculator = Calculator(5)

    // when
    try {
      calculator.divide(0)
    } catch (e: IllegalArgumentException) {

      if (e.message != "0으로 나눌 수 없습니다 !") {
        throw IllegalStateException("메세지가 다릅니다 ! ")
      }
      
      return
    } catch (e: Exception) {
      throw IllegalStateException()
    }
    
    throw IllegalStateException("기대하는 Exception 이 발생하지 않았습니다.")
    
  }
  
  
}