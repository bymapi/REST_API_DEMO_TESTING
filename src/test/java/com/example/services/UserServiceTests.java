package com.example.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.customExceptions.ResourceNotFoundException;
import com.example.security.entities.OurUser;
import com.example.security.repository.OurUserRepository;
import com.example.security.services.OurUserDetailsService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserServiceTests {

    @Mock
    private OurUserRepository ourUserRepository;

    @InjectMocks
    private OurUserDetailsService ourUserDetailsService;

    private OurUser ourUser;

    @BeforeEach
    void setUp() {

        ourUser = OurUser.builder()
                .email("permanentuser@mail.com")
                .password("1234")
                .role("ADMIN")
                .build();
    }

    @Test
    @DisplayName("***TEST SAVE USER WITH THROW EXCEPTION***")
    public void testSaveUserWithThrowException() {

        // GIVEN
        given(ourUserRepository.findByEmail(ourUser.getEmail()))
                .willReturn(Optional.of(ourUser));

        // WHEN

        assertThrows(ResourceNotFoundException.class, () -> {
            ourUserDetailsService.add(ourUser);
        });

        // THEN

        verify(ourUserRepository,never()).save((any(OurUser.class)));

    }

}
