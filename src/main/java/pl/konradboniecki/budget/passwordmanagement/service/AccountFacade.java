package pl.konradboniecki.budget.passwordmanagement.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pl.konradboniecki.chassis.tools.ChassisSecurityBasicAuthHelper;

import static java.util.Collections.singletonList;

@Data
@Service
public class AccountFacade {

    private AccountPasswordService accountPasswordService;
    private RestTemplate restTemplate;
    private String findAccountURL;

    @Autowired
    public AccountFacade(AccountPasswordService accountPasswordService, RestTemplate restTemplate, @Value("${budget.baseUrl.accountManagement}") String baseUrl) {
        setAccountPasswordService(accountPasswordService);
        setRestTemplate(restTemplate);
        setFindAccountURL(baseUrl + "/api/account-mgt/v1/accounts");
    }

    public void changePassword(String resetCodeFromUrl, String id){
        accountPasswordService.changePassword(resetCodeFromUrl, id);
    }

    public ResponseEntity<String> getAccountByEmail(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(ChassisSecurityBasicAuthHelper.getEncodedCredentials());
        String queryParameters = "?findBy=email";
        try {
            return restTemplate.exchange(findAccountURL + "/" + email + queryParameters,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);
        } catch (HttpClientErrorException e) {
            ResponseStatusException ex = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch account with email " + email, e);
            ex.printStackTrace();
            throw ex;
        }
    }
}
