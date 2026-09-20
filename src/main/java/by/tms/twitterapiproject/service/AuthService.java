package by.tms.twitterapiproject.service;

import by.tms.twitterapiproject.dto.AuthResponse;
import by.tms.twitterapiproject.dto.SigninRequest;
import by.tms.twitterapiproject.dto.SignupRequest;
import by.tms.twitterapiproject.dto.UserResponse;
import by.tms.twitterapiproject.entity.User;
import by.tms.twitterapiproject.exception.EmailAlreadyExistsException;
import by.tms.twitterapiproject.exception.InvalidCredentialsException;
import by.tms.twitterapiproject.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User(email, passwordEncoder.encode(request.password()));
        try {
            user = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new EmailAlreadyExistsException();
        }

        return buildResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse signin(SigninRequest request) {
        String email = normalizeEmail(request.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return buildResponse(user);
    }

    private AuthResponse buildResponse(User user) {
        return new AuthResponse(jwtService.generateAccessToken(user), UserResponse.from(user));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
