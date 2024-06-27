package com.example.store.service.implement;

import com.example.store.dto.RegistrationDTO;
import com.example.store.dto.UserDTO;
import com.example.store.entity.Provider;
import com.example.store.entity.Role;
import com.example.store.entity.User;
import com.example.store.exception.PasswordIncorrectException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.mapper.IUserMapper;
import com.example.store.repository.IUserRepository;
import com.example.store.security.oauth2.CustomOAuth2User;
import com.example.store.service.IUserService;
import com.example.store.util.FormattedDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    private static Logger logg = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private FormattedDateUtils formattedDateUtils;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserMapper userMapper;
    //    private final IdentificationRepository identificationRepository;
    private final JavaMailSender mailSender;

    public UserServiceImpl(IUserRepository userRepository, PasswordEncoder passwordEncoder, IUserMapper userMapper, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
//        this.identificationRepository = identificationRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found" + username);
        }
        User user = existingUser.get();
        boolean isChecked = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!isChecked) {
            throw new PasswordIncorrectException("Incorrect password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedDate(ZonedDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public List<UserDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTOs(users);
    }

    @Transactional
    @Override
    public boolean deleteUser(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    @Override
    public boolean resetPassword(Long id, String newPassword) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = existingUser.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedDate(ZonedDateTime.now());
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public UserDTO updateUser(Long id, RegistrationDTO registrationDTO) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = existingUser.get();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(registrationDTO.getPassword());
        user.setFullName(registrationDTO.getFullName());
        user.setEmail(registrationDTO.getEmail());
        user.setPhone(registrationDTO.getPhone());

//        Identification identification = user.getIdentification();
//        identification.setFullName(registrationDTO.getFullName());
//        identification.setPhone(registrationDTO.getPhone());
//        identification.setEmail(registrationDTO.getEmail());

        User savedUser = userRepository.save(user);
        UserDTO userDTO = userMapper.toDTO(savedUser);
        userDTO.setUpdatedDate(formattedDateUtils.convertToString(ZonedDateTime.now()));
        return userDTO;
    }

    @Override
    public UserDTO updateUser(String username, UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found"+username);
        }
        User user = existingUser.get();

        List<Role> roles = new ArrayList<>(user.getRoles());
        String identity = generateIdentity(roles.get(0).getName(), userDTO.getUsername());
        user.setIdentity(identity);

        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setUpdatedDate(ZonedDateTime.now());
        if (!userDTO.getUrlAvatar().isEmpty()) {
            user.setUrlAvatar(userDTO.getUrlAvatar());
        } user.setBirthDate(userDTO.getBirthDate());

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = optionalUser.get();

        user.setUsername(userDTO.getUsername());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setUpdatedDate(ZonedDateTime.now());
        if (!userDTO.getUrlAvatar().isEmpty()) {
            user.setUrlAvatar(userDTO.getUrlAvatar());
        } user.setBirthDate(userDTO.getBirthDate());

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Transactional
    @Override
    public Optional<User> findUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public UserDTO findUserByName(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found"+username);
        } User user = optionalUser.get();
        return userMapper.toDTO(user);
    }

    @Transactional
    @Override
    public String sendForgotPassword(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Email not found" + email);
        }
        User user = optionalUser.get();
        String resetToken = UUID.randomUUID().toString();
        user.setTokenResetPwd(resetToken);
        userRepository.save(user);

        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken + "&username=" + user.getUsername();
        sendEmail(email, "Password rest", "Click here to reset your password: " + resetLink);
        return "Send email successful";
    }

    @Transactional
    @Override
    public boolean updatePassword(String token, String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = optionalUser.get();
        String tokenRestPwd = user.getPassword();
        if (!token.equals(tokenRestPwd)) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setTokenResetPwd(null);
        userRepository.save(user);
        return true;
    }


    @Override
    public User findUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Not found user by email: "+email);
        }
//        logg.info("Found user by email: {}", email);
        User user = optionalUser.get();
        return user;
    }

    @Override
    public User loginWithFB(CustomOAuth2User customUser, Provider provider) {
        return null;
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    private String generateIdentity(String role, String username) {
        String strRand = String.valueOf(Math.round(Math.random() * 10000));
        String strRole = "";
        username = username.replaceAll(" ", "").toUpperCase();
        if(role.equals("CUSTOMER")){
            strRole ="CS";
        } else if (role.equals("ADMIN")) {
            strRole = "AD";
        } return strRole+"_"+username+"_"+strRand;
    }
}
