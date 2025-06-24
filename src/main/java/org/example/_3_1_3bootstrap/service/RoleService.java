package org.example._3_1_3bootstrap.service;

import org.example._3_1_3bootstrap.model.Role;
import org.example._3_1_3bootstrap.repository.RoleRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    private RoleRepo roleRepository;

    public RoleService(RoleRepo roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
