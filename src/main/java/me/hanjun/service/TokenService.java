package me.hanjun.service;

import lombok.RequiredArgsConstructor;
import me.hanjun.config.TokenProvider;
import me.hanjun.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {

        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        //-----------------------------------------------------------------------------
        Long userIdRedis = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userIdRedis);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
