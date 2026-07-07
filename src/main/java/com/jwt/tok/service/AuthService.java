package com.jwt.tok.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.tok.dto.ChangePasswordRequest;
import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.Dealer;
import com.jwt.tok.model.Employee;
import com.jwt.tok.model.Login;
import com.jwt.tok.model.Role;
import com.jwt.tok.repository.DealerRepository;
import com.jwt.tok.repository.EmployeeRepository;
import com.jwt.tok.repository.LoginRepository;
import com.jwt.tok.repository.RoleRepository;
import com.jwt.tok.util.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private RoleRepository roleRepository;
  
    @Autowired
    private JwtUtil jwtUtil;
  
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // REGISTER – role optional
    public void registerUser(Login login) {
        if (login.getUsername() == null || login.getPassword() == null) {
            throw new ApiException("Username and password required");
        }
        if (loginRepository.findByUsername(login.getUsername()) != null) {
            throw new ApiException("Username already exists");
        }

        login.setPassword(passwordEncoder.encode(login.getPassword()));
        login.setRole(null); // optional
        loginRepository.save(login);
    }

  
    public String generateTokenWithUser(Login user) {

        String role = user.getRole() != null
                ? user.getRole().getRole()
                : "NO_ROLE";

        String dealerCode = null;
        String employeeCode = null;

        if ("Dealer".equals(role)) {
            Dealer dealer = dealerRepository
                    .findByUsername(user.getUsername())
                    .orElse(null);

            if (dealer != null) {
                dealerCode = dealer.getDealerCode();
            }
        }

        if ("Employee".equals(role)) {
            Employee emp = employeeRepository
                    .findByUsername(user.getUsername())
                    .orElse(null);

            if (emp != null) {
                employeeCode = emp.getEmployeeCode();
            }
        }

        return jwtUtil.generateToken(
                user.getUsername(),
                role,
                dealerCode,
                employeeCode
        );
    }

    public Login getLoginByUsername(String username) {
        return loginRepository.findByUsername(username);
    }

    public Login validateTokenAndGetUser(String token) {
        String jwt = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwt);
        return loginRepository.findByUsername(username);
    }

//    public void assignRoleToUser(Long userId, Long roleId) {
//
//        Login user = loginRepository.findById(userId)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Role role = roleRepository.findById(roleId)
//            .orElseThrow(() -> new RuntimeException("Role not found"));
//
//        // 🔥 ROLE MUST BE "Admin" OR "Dealer"
//        if (!role.getRole().equals("Admin") && !role.getRole().equals("Dealer")) {
//            throw new RuntimeException("Invalid role assignment");
//        }
//
//        user.setRole(role);
//        loginRepository.save(user);
//    }
    public void assignRoleToUser(Long userId, Long roleId) {

        Login user = loginRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found"));

        // ❌ REMOVE THIS BLOCK (IMPORTANT)
        /*
        if (!role.getRole().equals("Admin") && !role.getRole().equals("Dealer")) {
            throw new RuntimeException("Invalid role assignment");
        }
        */

        // ✅ DIRECT ASSIGN (ANY ROLE)
        user.setRole(role);

        loginRepository.save(user);
    }
 // GET USER BY ID
    public Login getUserById(Long id) {
        return loginRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found with id " + id));
    }

    // GET ALL USERS
    public List<Login> getAllUsers() {
        return loginRepository.findAll();
    }
    
    public void changeCredentials(Map<String, String> req, String token) {

        String currentPassword = req.get("password");
        String newPassword = req.get("confirmPassword");
        String newUsername = req.get("username");

        Login user = validateTokenAndGetUser(token);

        if (user == null) {
            throw new ApiException("User not found ❌");
        }

        // ✅ CURRENT PASSWORD CHECK
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ApiException("Current password incorrect ❌");
        }

        String oldUsername = user.getUsername();

        // ✅ USERNAME UPDATE
        if (newUsername != null && !newUsername.equals(oldUsername)) {

            if (loginRepository.findByUsername(newUsername) != null) {
                throw new ApiException("Username already exists ❌");
            }

            user.setUsername(newUsername);

            // 🔥 Dealer update
            Dealer dealer = dealerRepository.findByUsername(oldUsername).orElse(null);
            if (dealer != null) {
                dealer.setUsername(newUsername);
                dealerRepository.save(dealer);
            }

            // 🔥 Employee update
            Employee emp = employeeRepository.findByUsername(oldUsername).orElse(null);
            if (emp != null) {
                emp.setUsername(newUsername);
                employeeRepository.save(emp);
            }
        }

        // ✅ PASSWORD UPDATE
        if (newPassword != null && !newPassword.isEmpty()) {

            String encoded = passwordEncoder.encode(newPassword);
            user.setPassword(encoded);

            // 🔥 Dealer update
            Dealer dealer = dealerRepository.findByUsername(user.getUsername()).orElse(null);
            if (dealer != null) {
                dealer.setPassword(encoded);
                dealerRepository.save(dealer);
            }

            // 🔥 Employee update
            Employee emp = employeeRepository.findByUsername(user.getUsername()).orElse(null);
            if (emp != null) {
                emp.setPassword(encoded);
                employeeRepository.save(emp);
            }
        }

        loginRepository.save(user);
    }
//    public void changePassword(String token, String newPassword, String confirmPassword) {
//
//        Login user = validateTokenAndGetUser(token);
//
//        if (newPassword == null || confirmPassword == null) {
//            throw new ApiException("Password required");
//        }
//
//        if (!newPassword.equals(confirmPassword)) {
//            throw new ApiException("Passwords do not match");
//        }
//
//        String encoded = passwordEncoder.encode(newPassword);
//
//        // 🔥 LOGIN TABLE UPDATE
//        user.setPassword(encoded);
//        loginRepository.save(user);
//
//        // 🔥 ROLE BASED UPDATE
//        String role = user.getRole().getRole();
//
//        if ("Employee".equals(role)) {
//            Employee emp = employeeRepository.findByUsername(user.getUsername()).orElse(null);
//            if (emp != null) {
//                emp.setPassword(encoded);
//                employeeRepository.save(emp);
//            }
//        }
//
//        if ("Dealer".equals(role)) {
//            Dealer dealer = dealerRepository.findByUsername(user.getUsername()).orElse(null);
//            if (dealer != null) {
//                dealer.setPassword(encoded);
//                dealerRepository.save(dealer);
//            }
//        }
//
//        // ✅ Admin sathi kahi vegla nahi — login table madhye update zale ki enough
//    }
    
    
//    public void changePassword(String token, Long userId, String newPassword, String confirmPassword) {
//
//        Login loggedUser = validateTokenAndGetUser(token);
//
//        if (newPassword == null || confirmPassword == null) {
//            throw new ApiException("Password required");
//        }
//
//        if (!newPassword.equals(confirmPassword)) {
//            throw new ApiException("Passwords do not match");
//        }
//
//        Login targetUser;
//
//        // ✅ ADMIN → dusryacha password change
//        if ("Admin".equals(loggedUser.getRole().getRole()) && userId != null) {
//            targetUser = loginRepository.findById(userId)
//                    .orElseThrow(() -> new ApiException("User not found"));
//        } else {
//            // ✅ Normal user → swatacha password
//            targetUser = loggedUser;
//        }
//
//        String encoded = passwordEncoder.encode(newPassword);
//
//        targetUser.setPassword(encoded);
//        loginRepository.save(targetUser);
//
//        // 🔥 Role-based update
//        String role = targetUser.getRole().getRole();
//
//        if ("Employee".equals(role)) {
//            Employee emp = employeeRepository.findByUsername(targetUser.getUsername()).orElse(null);
//            if (emp != null) {
//                emp.setPassword(encoded);
//                employeeRepository.save(emp);
//            }
//        }
//
//        if ("Dealer".equals(role)) {
//            Dealer dealer = dealerRepository.findByUsername(targetUser.getUsername()).orElse(null);
//            if (dealer != null) {
//                dealer.setPassword(encoded);
//                dealerRepository.save(dealer);
//            }
//        }
//    }
    
    public void changePassword(String token, Long userId, String newPassword, String confirmPassword) {

        Login loggedUser = validateTokenAndGetUser(token);

        if (newPassword == null || confirmPassword == null) {
            throw new ApiException("Password required");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new ApiException("Passwords do not match");
        }

        Login targetUser;

        if ("Admin".equals(loggedUser.getRole().getRole())) {

            if (userId != null) {
                targetUser = loginRepository.findById(userId)
                        .orElseThrow(() -> new ApiException("User not found"));
            } else {
                // 🔥 admin self change
                targetUser = loggedUser;
            }

        } else {
            // ❌ safety: non-admin can't change others
            if (userId != null) {
                throw new ApiException("You are not allowed to change others password ❌");
            }

            targetUser = loggedUser;
        }

        String encoded = passwordEncoder.encode(newPassword);

        targetUser.setPassword(encoded);
        loginRepository.save(targetUser);

        // 🔥 Sync with tables
        if ("Employee".equals(targetUser.getRole().getRole())) {
            employeeRepository.findByUsername(targetUser.getUsername())
                    .ifPresent(emp -> {
                        emp.setPassword(encoded);
                        employeeRepository.save(emp);
                    });
        }

        if ("Dealer".equals(targetUser.getRole().getRole())) {
            dealerRepository.findByUsername(targetUser.getUsername())
                    .ifPresent(dealer -> {
                        dealer.setPassword(encoded);
                        dealerRepository.save(dealer);
                    });
        }
    }
}
