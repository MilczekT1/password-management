package pl.konradboniecki.budget.passwordmanagement.model.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = ChangePasswordRequest.ChangePasswordRequestBuilder.class)
@Builder(builderClassName = "ChangePasswordRequestBuilder", toBuilder = true)
public class ChangePasswordRequest {
    private final String accountId;
    private final String newPassword;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ChangePasswordRequestBuilder {
    }
}
