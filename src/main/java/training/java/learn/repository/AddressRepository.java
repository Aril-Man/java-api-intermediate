package training.java.learn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.java.learn.entity.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    Address findByContactId(String id);
}
