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

  fun doReturn() {
    this.status = UserLoanStatus.RETURNED
  }

}


