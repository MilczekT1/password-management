package pl.konradboniecki.budget.passwordmanagement.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pl.konradboniecki.budget.passwordmanagement.model.json.Account;
import pl.konradboniecki.budget.passwordmanagement.model.json.ActivationLinkRequest;
import pl.konradboniecki.budget.passwordmanagement.model.json.ResetPasswordMailRequest;
import pl.konradboniecki.chassis.tools.ChassisSecurityBasicAuthHelper;

import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Data
@Slf4j
@Service
public class MailClient {

    private RestTemplate restTemplate;
    private String resetPasswordMailUrl;

    @Autowired
    public MailClient(RestTemplate restTemplate, @Value("${budget.baseUrl.mail}") String baseUrl) {
        setRestTemplate(restTemplate);
        setResetPasswordMailUrl(baseUrl + "/api/mail/v1/password-reset");
    }

    public void requestMailWithResetCode(ActivationLinkRequest alr) {
        Account acc = new Account()
                .setId(alr.getAccount().getId())
                .setFirstName(alr.getAccount().getFirstName())
                .setLastName(alr.getAccount().getLastName())
                .setEmail(alr.getAccount().getEmail());
        ResetPasswordMailRequest resetPasswordMailRequest = ResetPasswordMailRequest.builder()
                .account(acc)
                .resetCode(alr.getResetCode())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setBasicAuth(ChassisSecurityBasicAuthHelper.getEncodedCredentials());
        try {
            restTemplate.exchange(resetPasswordMailUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(resetPasswordMailRequest, headers),
                    String.class);
        } catch (HttpClientErrorException e) {
            String email = alr.getAccount().getEmail();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Email has not been set for " + email, e);
        }

        log.info("Mail request has been sent for " + alr.getAccount().getEmail());
    }
}
