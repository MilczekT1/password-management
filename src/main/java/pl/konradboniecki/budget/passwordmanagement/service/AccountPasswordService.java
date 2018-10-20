package pl.konradboniecki.budget.passwordmanagement.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.konradboniecki.budget.passwordmanagement.model.NewPasswordRequest;
import pl.konradboniecki.budget.passwordmanagement.model.json.ChangePasswordRequest;
import pl.konradboniecki.chassis.tools.ChassisSecurityBasicAuthHelper;

import java.util.Optional;

@Slf4j
@Data
@Service
public class AccountPasswordService {

    private NewPasswordRequestService newPasswordRequestService;
    private RestTemplate restTemplate;
    private String changePasswordUrl;

    @Autowired
    public AccountPasswordService(NewPasswordRequestService newPasswordRequestService, RestTemplate restTemplate, @Value("${budget.baseUrl.accountManagement}") String baseUrl) {
        setNewPasswordRequestService(newPasswordRequestService);
        setRestTemplate(restTemplate);
        setChangePasswordUrl(baseUrl + "/api/account-mgt/v1/accounts");
    }

    public void changePassword(String resetCodeFromUrl, String id){
        Optional<NewPasswordRequest> newPasswordOpt = newPasswordRequestService.findNewPasswordRequestById(id);
        if (newPasswordOpt.isPresent()){
            String correctResetCode = newPasswordOpt.get().getResetCode();
            if (resetCodeFromUrl.equals(correctResetCode)){
                String newPassword = newPasswordOpt.get().getNewPassword();
                sendRequestToChangePasswordInAccount(newPassword, id);
                newPasswordRequestService.deleteNewPasswordRequestById(id);
                log.info("Deleted NewPasswordRequest with id: " + id);
                return;
            }
            log.error("Invalid resetCodeFromUrl: " + resetCodeFromUrl + ", for id: " + id);
        } else {
            log.error("New password not found for id: " + id);
        }
    }

    private void sendRequestToChangePasswordInAccount(String newPassword, String accountId) {
        ChangePasswordRequest cpr = ChangePasswordRequest.builder()
                .accountId(accountId)
                .newPassword(newPassword)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(ChassisSecurityBasicAuthHelper.getEncodedCredentials());
        HttpEntity<?> entity = new HttpEntity<>(cpr, httpHeaders);

        restTemplate.exchange(changePasswordUrl + "/{accountId}/password",
                HttpMethod.PUT, entity, ChangePasswordRequest.class, accountId);

        log.info("Change password request sent for account with id: " + accountId + ".");
    }
}
