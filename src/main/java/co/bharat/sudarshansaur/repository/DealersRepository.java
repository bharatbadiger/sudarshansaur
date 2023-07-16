package co.bharat.sudarshansaur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.enums.UserStatus;

@RepositoryRestResource(collectionResourceRel = "dealers", path = "dealers")
public interface DealersRepository extends JpaRepository<Dealers,Long>{

	List<Dealers> findByMobileNoAndStatus(String mobileNo, UserStatus status);

	List<Dealers> findByMobileNo(String mobileNo);

	List<Dealers> findByStatus(UserStatus status);

}
