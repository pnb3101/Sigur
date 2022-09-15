package services;

import entities.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.DepartmentRepository;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Department getRandomDepartment() {
        List<Department> departments = departmentRepository.findAll();
        return departments.get(new Random().nextInt(departments.size()));
    }
}
