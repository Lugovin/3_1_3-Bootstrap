//package org.example._3_1_3bootstrap.config;
//
//
//import jakarta.annotation.PostConstruct;
//
//
//import org.example._3_1_3bootstrap.model.Role;
//import org.example._3_1_3bootstrap.model.User;
//import org.example._3_1_3bootstrap.repository.RoleRepo;
//import org.example._3_1_3bootstrap.repository.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//
//
//@Component
//public class DatabaseInit {
//
//    public Role adminRole = new Role(1L, "ADMIN");
//    public Role userRole = new Role(2L, "USER");
//
//    private final UserRepo userRepository;
//    private final RoleRepo roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public DatabaseInit(UserRepo userRepository, RoleRepo roleRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @PostConstruct
//    public void init() {
//
//        roleRepository.save(adminRole);
//        roleRepository.save(userRole);
//
//        User user1 = new User("Bill", "Gates", 60, "faja@google.com","1111");
//        user1.setPassword(passwordEncoder.encode(user1.getPassword()));
//        User user2 = new User("John", "Smith", 33, "ddd@mail.ru", "0000");
//        user2.setPassword(passwordEncoder.encode(user2.getPassword()));
//        User user3 = new User("Николай", "Луговин", 40, "lugovin@gmail.com", "9999");
//        user3.setPassword(passwordEncoder.encode(user3.getPassword()));
//
//        user1.setRole(adminRole);
//        user2.setRole(userRole);
//        user3.setRole(userRole);
//        user3.setRole(adminRole);
//
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//
//        System.out.println("База данных заполнена начальными данными");
//    }
//}
