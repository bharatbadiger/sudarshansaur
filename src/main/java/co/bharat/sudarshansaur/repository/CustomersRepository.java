package co.bharat.sudarshansaur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.enums.UserStatus;

//@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomersRepository extends JpaRepository<Customers,Long>{

	List<Customers> findByMobileNoAndEmailAndStatus(String mobileNo, String email, UserStatus status);
	
	List<Customers> findByMobileNoAndEmail(String mobileNo, String email);
	
	List<Customers> findByMobileNoAndStatus(String mobileNo, UserStatus status);

	List<Customers> findByMobileNo(String mobileNo);

	List<Customers> findByStatus(UserStatus status);
	
	Optional<Customers> findByEmailAndPassword(String email,String password);
	
    @Query("SELECT c.status, COUNT(1) FROM Customers c GROUP BY c.status")
    List<Object[]> countCustomersByStatus();

}
