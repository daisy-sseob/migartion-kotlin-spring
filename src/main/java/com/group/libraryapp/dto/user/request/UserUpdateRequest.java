package com.group.libraryapp.dto.user.request;

public class UserUpdateRequest {

  private Long id;
  private String name;

  public UserUpdateRequest(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}
