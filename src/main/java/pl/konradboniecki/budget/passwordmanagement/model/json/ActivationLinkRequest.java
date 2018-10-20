package pl.konradboniecki.budget.passwordmanagement.model.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class ActivationLinkRequest implements Serializable {

    private Account account;
    private String resetCode;
}
