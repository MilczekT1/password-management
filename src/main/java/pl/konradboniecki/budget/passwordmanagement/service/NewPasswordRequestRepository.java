package pl.konradboniecki.budget.passwordmanagement.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.konradboniecki.budget.passwordmanagement.model.NewPasswordRequest;

import java.util.Optional;

@Repository
public interface NewPasswordRequestRepository extends CrudRepository<NewPasswordRequest, String> {

    Optional<NewPasswordRequest> findById(String id);
    NewPasswordRequest save(NewPasswordRequest entity);
    long count();
    void deleteById(String id);
    boolean existsById(String id);
}
