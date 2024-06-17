//package com.example.store.controllers;
//
//import com.example.store.dto.IdentificationDTO;
//import com.example.store.exception.UserNotFoundException;
//import com.example.store.service.IUploadFileService;
//import com.example.store.service.IdentificationService;
//import io.jsonwebtoken.ExpiredJwtException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDate;
//
//@RestController
//@RequestMapping("/api/user/identification")
//@CrossOrigin(origins = "http://localhost:3000")
//public class IdentificationController {
//
//    private Logger logg = LoggerFactory.getLogger(IdentificationController.class);
//    private final IdentificationService identificationService;
//    private final IUploadFileService uploadFileService;
//
//    public IdentificationController(IdentificationService identificationService, IUploadFileService uploadFileService) {
//        this.identificationService = identificationService;
//        this.uploadFileService = uploadFileService;
//    }
//
//    @GetMapping
//    public ResponseEntity<?> getIdentification(Authentication authentication) {
//        try {
//            String username = authentication.getName();
//            return new ResponseEntity<>(identificationService.getIdentification(username), HttpStatus.OK);
//        } catch (AuthenticationException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//        } catch (UserNotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (AccessDeniedException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> updateIdentification(Authentication auth,
//                                                  @RequestParam(value = "image", required = false) MultipartFile image,
//                                                  @RequestParam(value = "fullName") String fullName,
//                                                  @RequestParam(value = "phone") String phone,
//                                                  @RequestParam(value = "birthDate") LocalDate birthDate,
//                                                  @RequestParam(value = "email") String email) throws IOException {
//        try {
//            String imgUrl = "";
//            if (image != null) {
//                imgUrl = uploadFileService.uploadFile(image);
//            }
//            String username = auth.getName();
//            logg.info("Dto: {}, {}, {}, {}, {}, {}", username, fullName, phone, birthDate, email, imgUrl);
//            IdentificationDTO dto = new IdentificationDTO(fullName, birthDate, phone, email, imgUrl);
//            return new ResponseEntity<>(identificationService.updateIdentification(username, dto), HttpStatus.OK);
//        } catch (AuthenticationException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//        } catch (UserNotFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (AccessDeniedException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
