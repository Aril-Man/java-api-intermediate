package training.java.learn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training.java.learn.entity.Address;

public interface AddressRepository extends JpaRepository<Address, String> {
}
