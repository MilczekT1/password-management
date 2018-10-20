package pl.konradboniecki.budget.passwordmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pl.konradboniecki.chassis.tools.HashGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "new_password")
public class NewPasswordRequest {

    @Id
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "new_password")
    private String newPassword;
    @Column(name = "created")
    private Instant created;
    @Column(name = "reset_code")
    private String resetCode;

    public NewPasswordRequest(NewPasswordForm newPasswordForm) {
        setCreated(Instant.now());
        setNewPassword(newPasswordForm.getPassword());
    }

    public NewPasswordRequest(NewPasswordForm newPasswordForm, String accountId, String resetCode) {
        this(newPasswordForm);
        setAccountId(accountId);
        setResetCode(resetCode);
    }

    public void setNewPassword(String password){
        this.newPassword = new HashGenerator().hashPassword(password);
    }
}
