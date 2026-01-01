package springboot.aviation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.aviation.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
