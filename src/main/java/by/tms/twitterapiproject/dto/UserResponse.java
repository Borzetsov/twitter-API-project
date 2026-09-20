package by.tms.twitterapiproject.dto;

import by.tms.twitterapiproject.entity.User;

public record UserResponse(Long id, String email) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail());
    }
}
