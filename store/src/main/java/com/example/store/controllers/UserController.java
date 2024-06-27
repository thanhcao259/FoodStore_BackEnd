package com.example.store.controllers;

import com.example.store.dto.UserDTO;
import com.example.store.exception.UserNotFoundException;
import com.example.store.service.IUploadFileService;
import com.example.store.service.IUserService;
import com.example.store.service.implement.UploadFileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private Logger logg = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;
    @Autowired
    private IUploadFileService uploadFileService;
    public UserController(IUserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user/identification")
    public ResponseEntity<?> getUserInfo(Authentication auth) {
        try {
            String username = auth.getName();
            UserDTO userDTO = userService.findUserByName(username);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/get-users")
    public ResponseEntity<?> getAllUser() {
        try {
            List<UserDTO> dtoList = userService.getAllUser();
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/user/identification", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserInfo(Authentication auth,
                                            @RequestParam(value = "image", required = false) MultipartFile image,
                                            @RequestParam(value = "fullName") String fullName,
                                            @RequestParam(value = "phone") String phone,
                                            @RequestParam(value = "birthDate") LocalDate birthDate,
                                            @RequestParam(value = "email") String email) throws IOException {
        try {
            String username = auth.getName();
            String imgUrl = "";

            if(image != null) {
                imgUrl = uploadFileService.uploadFile(image);
            }
            UserDTO userDTO = new UserDTO(username,fullName,email, phone, imgUrl, birthDate);
            return new ResponseEntity<>(userService.updateUser(username, userDTO), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication auth,
                                            @RequestParam("password") String password,
                                            @RequestParam("newPassword") String newPassword) {
        try {
            String username = auth.getName();
            boolean isChanged = userService.changePassword(username, password, newPassword);
            return new ResponseEntity<>(isChanged, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<?> deleteUser(Authentication auth, @PathVariable Long id) {
        try {
            String username = auth.getName();
            boolean isDeleted = userService.deleteUser(id);
            return new ResponseEntity<>(isDeleted, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/user/{id}")
    public ResponseEntity<?> updateUser(Authentication auth, @RequestBody UserDTO userDTO,
                                        @PathVariable Long id) {
        try {
            String username = auth.getName();
            UserDTO updateUser = userService.updateUser(id, userDTO);
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("id") Long userId,
                                           @RequestParam("password") String password) {
        try {
            boolean isReset = userService.resetPassword(userId, password);
            return new ResponseEntity<>(isReset, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        try {
            return new ResponseEntity<>(userService.sendForgotPassword(email), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam("tokenForgot") String token,
                                            @RequestParam("username") String username,
                                            @RequestParam("password") String password) {
        try {
            return new ResponseEntity<>(userService.updatePassword(token, username, password), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
