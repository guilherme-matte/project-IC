package control.invest.IC.authentication.repositories;

import control.invest.IC.authentication.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByCpf(String cpf);

    Optional<UserModel> findByEmail(String email);
}
