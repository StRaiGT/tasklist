package com.example.tasklist.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response user data")
public class UserResponse {
    @Schema(description = "User id",
            example = "1")
    private Long id;

    @Schema(description = "User name",
            example = "William Emard")
    private String name;

    @Schema(description = "User email",
            example = "william.emard@gmailRU.com")
    private String username;
}
