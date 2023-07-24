package co.bharat.sudarshansaur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.enums.UserStatus;

//@RepositoryRestResource(collectionResourceRel = "dealers", path = "dealers")
public interface DealersRepository extends JpaRepository<Dealers,Long>{

	List<Dealers> findByMobileNoAndEmailAndStatus(String mobileNo, String email, UserStatus status);
	
	List<Dealers> findByMobileNoAndEmail(String mobileNo, String email);
	
	List<Dealers> findByMobileNoAndStatus(String mobileNo, UserStatus status);

	List<Dealers> findByMobileNo(String mobileNo);

	List<Dealers> findByStatus(UserStatus status);
	
	Optional<Dealers> findByEmailAndPassword(String email, String password);

}
