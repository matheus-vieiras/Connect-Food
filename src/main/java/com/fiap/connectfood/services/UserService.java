package com.fiap.connectfood.services;

import com.fiap.connectfood.enums.UserTypeEnum;
import com.fiap.connectfood.model.UserLoginModel;
import com.fiap.connectfood.model.UserModel;
import com.fiap.connectfood.repository.AddressRepository;
import com.fiap.connectfood.repository.UserRepository;
import com.fiap.connectfood.services.exceptions.DatabaseException;
import com.fiap.connectfood.services.exceptions.ResourceCnpjNotFoundException;
import com.fiap.connectfood.services.exceptions.ResourceNotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    public Optional<UserModel> registerUser(UserModel userModel) {
        if (Objects.nonNull(userRepository.findByEmail(userModel.getEmail()))) {
            return null;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String senhaEncoder = encoder.encode(userModel.getPassword());
        userModel.setPassword(senhaEncoder);

        userModel.getAddresses().stream().forEach(a -> addressRepository.save(a));

        return Optional.of(userRepository.save(userModel));
    }

    public Optional<UserLoginModel> login(Optional<UserLoginModel> user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<UserModel> usuario = Optional.ofNullable(userRepository.findByEmail(user.get().getEmail()));

        if (usuario.isPresent()) {
            if (encoder.matches(user.get().getPassword(), usuario.get().getPassword())) {

                String authHeader = createAuthToken(user);
                populateUserLogin(user, usuario, authHeader);

                return user;
            }
        }

        return null;
    }

    private String createAuthToken(Optional<UserLoginModel> user) {
        String auth = user.get().getEmail() + ":" + user.get().getPassword();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));

        return "Basic " + new String(encodedAuth);
    }

    private Optional<UserLoginModel> populateUserLogin(Optional<UserLoginModel> userLogin, Optional<UserModel> userModel, String authHeader) {
        userLogin.get().setToken(authHeader);
        userLogin.get().setId(userModel.get().getId());
        userLogin.get().setNome(userModel.get().getName());
        userLogin.get().setPhoto(userModel.get().getPhoto());
        userLogin.get().setType(userModel.get().getType());

        return userLogin;
    }

    public void deleteUser(int id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    public Optional<UserModel> editUser(int id, UserModel userModel) {
        try {
            userModel = userRepository.getReferenceById(id);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            String senhaEncoder = encoder.encode(userModel.getPassword());
            userModel.setPassword(senhaEncoder);

            return Optional.of(userRepository.save(userModel));

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }


    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel getUserById(int id) {
        Optional<UserModel> userModel = userRepository.findById(id);
        return userModel.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public UserModel getUserByCnpj(String cnpj) {
        Optional<UserModel> userModel = Optional.ofNullable(userRepository.findByCnpj(cnpj));
        return userModel.orElseThrow(() -> new ResourceCnpjNotFoundException(cnpj));
    }

    public List<UserModel> findAllByName(String name) {
        return userRepository.findAllByName(name);
    }

    public List<UserModel> findAllByType(UserTypeEnum type) {
        return userRepository.findAllByType(type);
    }

    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Integer> findAllCurtidos(String cnpj) {
        return userRepository.findByCnpj(cnpj).getCurtido();
    }

    public void curtir(Integer id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário inexistente"));
        userModel.getCurtido().add(id);
        userRepository.save(userModel);
    }

}
