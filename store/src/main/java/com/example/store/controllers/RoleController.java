package com.example.store.controllers;

import com.example.store.dto.RoleUserDTO;
import com.example.store.repository.IRoleRepository;
import com.example.store.service.IRoleUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class RoleController {

    private final IRoleUserService roleUserService;

    public RoleController(IRoleUserService roleUserService) {
        this.roleUserService = roleUserService;
    }

    @PutMapping("/set-role")
    public ResponseEntity<?> setRole(@RequestParam("idUser")Long idUser,
                                     @RequestParam("idRole")Long idRole) {
        try {
            RoleUserDTO roleUserDTO = new RoleUserDTO(idUser, idRole);
            return new ResponseEntity<>(roleUserService.setRole(roleUserDTO), HttpStatus.OK);
        }  catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
