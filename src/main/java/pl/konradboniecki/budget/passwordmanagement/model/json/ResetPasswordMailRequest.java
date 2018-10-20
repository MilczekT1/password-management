package pl.konradboniecki.budget.passwordmanagement.model.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = ResetPasswordMailRequest.ResetPasswordDetailsBuilder.class)
@Builder(builderClassName = "ResetPasswordDetailsBuilder", toBuilder = true)
public class ResetPasswordMailRequest {
    private final Account account;
    private final String resetCode;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ResetPasswordDetailsBuilder {
    }
}
