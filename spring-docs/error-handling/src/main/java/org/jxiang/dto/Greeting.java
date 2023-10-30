package org.jxiang.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Greeting {

  @NotNull(message = "message cannot be null")
  @NotEmpty(message = "message cannot be empty")
  private String message;

  @NotNull(message = "from cannot be null")
  @NotEmpty(message = "from cannot be empty")
  private String from;

  @NotNull(message = "to cannot be null")
  @NotEmpty(message = "to cannot be empty")
  private String to;

}
