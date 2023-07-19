package co.bharat.sudarshansaur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.bharat.sudarshansaur.entity.Stockists;
import co.bharat.sudarshansaur.enums.UserStatus;

//@RepositoryRestResource(collectionResourceRel = "stockists", path = "stockists")
public interface StockistsRepository extends JpaRepository<Stockists,Long>{

	List<Stockists> findByMobileNoAndStatus(String mobileNo, UserStatus status);

	List<Stockists> findByMobileNo(String mobileNo);

	List<Stockists> findByStatus(UserStatus status);

}
