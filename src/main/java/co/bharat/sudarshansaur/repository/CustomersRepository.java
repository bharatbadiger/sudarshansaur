package co.bharat.sudarshansaur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.enums.UserStatus;

//@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomersRepository extends JpaRepository<Customers,Long>{

	List<Customers> findByMobileNoAndStatus(String mobileNo, UserStatus status);

	List<Customers> findByMobileNo(String mobileNo);

	List<Customers> findByStatus(UserStatus status);

}
