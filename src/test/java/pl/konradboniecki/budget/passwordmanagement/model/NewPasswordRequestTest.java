package pl.konradboniecki.budget.passwordmanagement.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import pl.konradboniecki.chassis.tools.HashGenerator;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class NewPasswordRequestTest {

    private NewPasswordForm newPasswordForm;

    @BeforeAll
    void setUp() {
        newPasswordForm = new NewPasswordForm();
        newPasswordForm.setEmail("test@mail.com");
        newPasswordForm.setPassword("pass");
        newPasswordForm.setRepeatedPassword("pass");
    }

    @Test
    void whenNewObj_thenDefaultsAreNotSet(){
        // When:
        NewPasswordRequest newPasswordRequest = new NewPasswordRequest();
        // Then:
        assertAll(
                () -> assertNull(newPasswordRequest.getAccountId()),
                () -> assertNull(newPasswordRequest.getCreated()),
                () -> assertNull(newPasswordRequest.getNewPassword()),
                () -> assertNull(newPasswordRequest.getResetCode())
        );
    }

    @Test
    void givenNewObj_whenSetNewPasswd_thenPasswdIsHashed(){
        // Given:
        NewPasswordRequest newPasswordRequest = new NewPasswordRequest();
        // When:
        newPasswordRequest.setNewPassword("pass");
        // Then:
        String hashedPassword = new HashGenerator().hashPassword("pass");
        assertEquals(hashedPassword, newPasswordRequest.getNewPassword());
    }

    @Test
    void givenNewPasswordForm_whenInitFromThisForm_thenPasswordsAreSet(){
        // Given:
        NewPasswordForm newPasswordForm = this.newPasswordForm;
        // When:
        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(newPasswordForm);
        // Then:
        String hashedPassword = new HashGenerator().hashPassword("pass");
        assertAll(
                () -> assertEquals(hashedPassword, newPasswordRequest.getNewPassword()),
                () -> assertNotNull(newPasswordRequest.getCreated()),
                () -> assertNull(newPasswordRequest.getResetCode()),
                () -> assertNull(newPasswordRequest.getAccountId())
        );
    }

    @Test
    void givenNewPasswordFormAndParams_whenInit_thenPropertiesAreSet(){
        // Given:
        NewPasswordForm newPasswordForm = this.newPasswordForm;
        String accId = UUID.randomUUID().toString();
        String resetCode = "blabla";
        String hashedPassword = new HashGenerator().hashPassword("pass");
        // When:
        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(newPasswordForm, accId, resetCode);
        // Then:
        assertAll(
                () -> assertEquals(hashedPassword, newPasswordRequest.getNewPassword()),
                () -> assertNotNull(newPasswordRequest.getCreated()),
                () -> assertEquals(resetCode, newPasswordRequest.getResetCode()),
                () -> assertEquals(accId, newPasswordRequest.getAccountId())
        );
    }
}
