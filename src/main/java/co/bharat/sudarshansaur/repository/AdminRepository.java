package co.bharat.sudarshansaur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.bharat.sudarshansaur.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {
	
}
