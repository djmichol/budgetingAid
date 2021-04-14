package pl.michol.budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.michol.budget.repository.entity.RegisterEntity;

@Repository
public interface RegisterRepository extends JpaRepository<RegisterEntity, Long> {

    RegisterEntity findByRegisterName(String registerName);

}
