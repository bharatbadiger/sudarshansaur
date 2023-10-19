package co.bharat.sudarshansaur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.enums.UserStatus;

//@RepositoryRestResource(collectionResourceRel = "stockists", path = "stockists")
public interface StockistsRepository extends JpaRepository<Stockists,Long>{

	Stockists findByMobileNoAndEmailAndStatus(String mobileNo, String email, UserStatus status);
	
	Stockists findByMobileNoAndEmail(String mobileNo, String email);
	
	Stockists findByMobileNoAndStatus(String mobileNo, UserStatus status);

	Stockists findByMobileNo(String mobileNo);

	List<Stockists> findByStatus(UserStatus status);
	
	Optional<Stockists> findByStockistCode(String stockistCode);
	
//	Optional<Stockists> findByEmailAndPassword(String email, String password);

}
