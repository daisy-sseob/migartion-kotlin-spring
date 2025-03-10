package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.domain.user.User
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class UserLoanHistory(

  @ManyToOne
  val user: User,

  val bookName: String,

  var status: UserLoanStatus = UserLoanStatus.LOANED,

  @Id
  @GeneratedValue
  val id: Long? = null,

  ) {
  
  val isReturn: Boolean
    get() = status == UserLoanStatus.RETURNED

  fun doReturn() {
    this.status = UserLoanStatus.RETURNED
  }

  companion object {
    fun fixture(
      user: User,
      bookName: String,
      status: UserLoanStatus = UserLoanStatus.LOANED): UserLoanHistory? {
      
      return UserLoanHistory(user, bookName, status)
    }
  }

}


