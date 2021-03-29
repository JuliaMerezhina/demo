package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo){
        this.roleRepo = roleRepo;
    }

    @Override
    public Role getRoleByName(String role) {
        return roleRepo.findRoleByRolename(role);
    }

}
