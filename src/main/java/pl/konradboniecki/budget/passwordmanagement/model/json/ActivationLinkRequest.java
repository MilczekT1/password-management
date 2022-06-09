package pl.konradboniecki.budget.passwordmanagement.model.json;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ActivationLinkRequest {

    private Account account;
    private String resetCode;
}
