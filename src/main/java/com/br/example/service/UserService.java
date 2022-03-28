package com.br.example.service;

import com.br.example.config.ModelMapperConf;
import com.br.example.dto.UserDTO;
import com.br.example.dto.form.UserForm;
import com.br.example.model.User;
import com.br.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ModelMapperConf modelMapper;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, ModelMapperConf modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<UserDTO> findAll() {
        List<User> usersList = userRepository.findAll();
        return usersList.stream().map(UserDTO::from).collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        return UserDTO.from(userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }));
    }

    public UserDTO create(UserForm userForm){
        if(userRepository.findByEmail(userForm.getEmail()).isPresent()){
            logger.error("Email already exists {}");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }

        return UserDTO.from(userRepository.save(User.from(userForm)));
    }

    public UserDTO update(UserForm userForm, Long id){
        var userFound = userRepository.findById(id).orElseThrow(() ->{
            logger.error("User not found {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        modelMapper.modelMapper().map(userForm, userFound);
        return UserDTO.from(userRepository.save(userFound));
    }

    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("Id not found {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        userRepository.delete(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByUsername(email);
    }
}
