package com.group.libraryapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibraryAppApplication 

/*
* class body 바깥에 선언되는 함수는 static 함수로 취급된다.
* */
fun main(args: Array<String>) {
  runApplication<LibraryAppApplication>(*args)
}