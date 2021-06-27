package sg.edu.iss.caps.repo;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.iss.caps.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	

	User findByEmailAndPassword(@Valid String email, String password);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.firstname, ' ', u.surname, ' ', u.email) LIKE %?1%")
	public List<User> search(String keyword);
	
}
