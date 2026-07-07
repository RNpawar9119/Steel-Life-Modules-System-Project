package com.jwt.tok.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.Employee;
import com.jwt.tok.model.Login;
import com.jwt.tok.model.Role;
import com.jwt.tok.repository.EmployeeRepository;
import com.jwt.tok.repository.LoginRepository;
import com.jwt.tok.repository.RoleRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final LoginRepository loginRepo;
    private final RoleRepository roleRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public EmployeeService(EmployeeRepository employeeRepo,
                           LoginRepository loginRepo,
                           RoleRepository roleRepo) {
        this.employeeRepo = employeeRepo;
        this.loginRepo = loginRepo;
        this.roleRepo = roleRepo;
    }

    private String generateEmployeeCode() {
        long count = employeeRepo.count() + 1;
        return String.format("EMP/%03d", count);
    }

//    public Employee create(Employee employee) {
//        employee.setEmployeeCode(null);
//
//        // 🔴 Username unique
//        if (loginRepo.findByUsername(employee.getUsername()) != null) {
//            throw new ApiException("Username already exists");
//        }
//
//        // 🔴 Password match
//        if (!employee.getPassword().equals(employee.getConfirmPassword())) {
//            throw new ApiException("Passwords do not match");
//        }
//
//        // 🔐 Encode password
//        String encoded = encoder.encode(employee.getPassword());
//        employee.setPassword(encoded);
//
//        // ✅ AUTO employeeCode
//        employee.setEmployeeCode(generateEmployeeCode());
//
//        // 1️⃣ Save Employee
//        Employee savedEmployee = employeeRepo.save(employee);
//
//        // 2️⃣ Create Login entry
//        Login login = new Login();
//        login.setUsername(employee.getUsername());
//        login.setPassword(encoded);
//
//        Role empRole = roleRepo.findByRole("Employee");
//        login.setRole(empRole);
//
//        loginRepo.save(login);
//
//        return savedEmployee;
//    }
    
    public Employee create(Employee employee) {
        employee.setEmployeeCode(null);

        // 🔴 Username unique
        if (loginRepo.findByUsername(employee.getUsername()) != null) {
            throw new ApiException("Username already exists");
        }

        // 🔴 Password match
        if (!employee.getPassword().equals(employee.getConfirmPassword())) {
            throw new ApiException("Passwords do not match");
        }

        // 🔐 Encode password
        String encoded = encoder.encode(employee.getPassword());
        employee.setPassword(encoded);

        // ✅ ACTIVE DEFAULT
        employee.setActive(true);

        // ✅ AUTO employeeCode
        employee.setEmployeeCode(generateEmployeeCode());

        // 1️⃣ Save Employee
        Employee savedEmployee = employeeRepo.save(employee);

        // 2️⃣ Create Login entry
        Login login = new Login();
        login.setUsername(employee.getUsername());
        login.setPassword(encoded);

        // ✅ ACTIVE DEFAULT
        login.setActive(true);

        Role empRole = roleRepo.findByRole("Employee");
        login.setRole(empRole);

        loginRepo.save(login);

        return savedEmployee;
    }
    
    public List<Employee> getAll() {
        return employeeRepo.findAll();
    }

    public Employee getById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new ApiException("Employee not found"));
    }

    public Employee update(Long id, Employee emp) {

        Employee existing = getById(id);

        existing.setEmployeeName(emp.getEmployeeName());
        existing.setAddress(emp.getAddress());
        existing.setMobileNo(emp.getMobileNo());
        existing.setDateOfJoining(emp.getDateOfJoining());
        existing.setDateOfBirth(emp.getDateOfBirth());
        existing.setDesignation(emp.getDesignation());
        existing.setDepartmentName(emp.getDepartmentName());
        existing.setBankName(emp.getBankName());
        existing.setAccountNo(emp.getAccountNo());
        existing.setIfscCode(emp.getIfscCode());
        existing.setEmployeeCode(emp.getEmployeeCode());
        existing.setRemark(emp.getRemark());

        // 🔐 Password update optional
        if (emp.getPassword() != null && !emp.getPassword().isEmpty()) {
            String encoded = encoder.encode(emp.getPassword());
            existing.setPassword(encoded);

            Login login = loginRepo.findByUsername(existing.getUsername());
            login.setPassword(encoded);
            loginRepo.save(login);
        }

        return employeeRepo.save(existing);
    }

    public void delete(Long id) {
        employeeRepo.deleteById(id);
    }
    public Login getLoginByUsername(String username) {
        return loginRepo.findByUsername(username);
    }

    public void saveLogin(Login login) {
        loginRepo.save(login);
    }
    
    public void changeCredentials(String username, Employee emp) {

        Employee existing = employeeRepo.findByUsername(username)
                .orElseThrow(() -> new ApiException("Employee not found"));

        Login login = loginRepo.findByUsername(username);

        if (login == null) {
            throw new ApiException("Login record not found");
        }

        // ✅ CURRENT PASSWORD CHECK
        if (!encoder.matches(emp.getPassword(), existing.getPassword())) {
            throw new ApiException("Current password is incorrect ❌");
        }

        // ✅ USERNAME UPDATE
        if (emp.getUsername() != null && !emp.getUsername().equals(username)) {

            if (loginRepo.findByUsername(emp.getUsername()) != null) {
                throw new ApiException("Username already exists ❌");
            }

            login.setUsername(emp.getUsername());
            existing.setUsername(emp.getUsername());
        }

        // ✅ PASSWORD UPDATE
        if (emp.getConfirmPassword() != null && !emp.getConfirmPassword().isEmpty()) {

            String encoded = encoder.encode(emp.getConfirmPassword());

            login.setPassword(encoded);
            existing.setPassword(encoded);
        }

        loginRepo.save(login);
        employeeRepo.save(existing);
    }
    
    public List<Employee> autoSearch(String keyword) {

        if (keyword == null) keyword = "";

        return employeeRepo
            .findTop10ByEmployeeNameContainingIgnoreCaseOrEmployeeCodeContainingIgnoreCase(
                keyword, keyword
            );
    }
}
