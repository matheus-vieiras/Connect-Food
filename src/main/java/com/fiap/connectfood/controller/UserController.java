package com.fiap.connectfood.controller;

import com.fiap.connectfood.dto.LocationDto;
import com.fiap.connectfood.enums.UserTypeEnum;
import com.fiap.connectfood.model.UserLoginModel;
import com.fiap.connectfood.model.UserModel;
import com.fiap.connectfood.services.LocationService;
import com.fiap.connectfood.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(value = "*", allowedHeaders = "*")
@Api(tags = "API de Usuários")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    LocationService locationService;

    @PostMapping("/login")
    @ApiOperation("Login")
    public ResponseEntity<UserLoginModel> login(@RequestBody Optional<UserLoginModel> user) {
        return getUserService().login(user).map(resp -> ResponseEntity.ok(resp))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    @ApiOperation("Cadastrar um usuário")
    public ResponseEntity<Object> registerUser(@RequestBody UserModel userModel) {
        if (getUserService().registerUser(userModel) == null)
        {
            return ResponseEntity.ok().body("Usuário ja existe no sistema.");
        }

        return ResponseEntity.ok().body("Usuário cadastrado com sucesso!");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar um usuário")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") int id) {
        getUserService().deleteUser(id);

        return ResponseEntity.ok().body("O usuário " + id + " foi deletado!");
    }

    @PutMapping("/{id}")
    @ApiOperation("Atualizar um usuário")
    public ResponseEntity<Object> editUser(@PathVariable(value = "id") int id,
                                           @RequestBody UserModel user) {
        getUserService().editUser(id, user);

        return ResponseEntity.ok().body("O usuário " + id + " foi atualizado!");
    }

    @GetMapping
    @ApiOperation("Trazer todos os usuários")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(getUserService().getAllUsers());
    }

    @GetMapping("/{id}")
    @ApiOperation("Trazer usuário por ID")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "id") int id) {
        Optional<UserModel> instituicaoModelOptional = getUserService().getUserById(id);

        return ResponseEntity.ok(instituicaoModelOptional);
    }

    @GetMapping("/cnpj/{cnpj}")
    @ApiOperation("Trazer usuário por CNPJ")
    public ResponseEntity<UserModel> getByCnpj(@PathVariable(value = "cnpj") String cnpj) {
        return ResponseEntity.ok(getUserService().getUserByCnpj(cnpj));
    }

    @GetMapping("/donors")
    @ApiOperation("Trazer usuário por tipo (Doador)")
    public ResponseEntity<List<UserModel>> getByTypeDonors() {
        return ResponseEntity.ok(getUserService().findAllByType(UserTypeEnum.DONOR));
    }

    @GetMapping("/recievers")
    @ApiOperation("Trazer usuário por tipo (Recebedor)")
    public ResponseEntity<List<UserModel>> getByTypeRecievers() {
        return ResponseEntity.ok(getUserService().findAllByType(UserTypeEnum.RECEIVER));
    }

    @PostMapping("/curtir/{id}")
    public ResponseEntity<String> curtir(@PathVariable Integer id) {
        userService.curtir(id);
        return ResponseEntity.ok().body("Usuário Curtido!!!");
    }

    @GetMapping("/curtidos/{cnpj}")
    public ResponseEntity<List<Integer>> listarCurtidos(@PathVariable String cnpj) {
        List<Integer> usuariosCurtidos = userService.findAllCurtidos(cnpj);
        return ResponseEntity.ok(usuariosCurtidos);
    }

    //WEB Endpoints
    @GetMapping("/total-doadores")
    @ApiOperation("Trazer total de doadores")
    public ResponseEntity<Integer> getTotalDonorsQunatity() {
        int donorsQtd = userService.findAllByType(UserTypeEnum.DONOR).size();
        return ResponseEntity.ok(donorsQtd);
    }

    @GetMapping("/total-recebedores")
    @ApiOperation("Trazer total de recebedores")
    public ResponseEntity<Integer> getTotalReceiversQunatity() {
        int receiversQtd = userService.findAllByType(UserTypeEnum.RECEIVER).size();
        return ResponseEntity.ok(receiversQtd);
    }

    @GetMapping("get-distance/{cnpj}")
    public ResponseEntity<Map<String, String>> getDistanceBetweenUsers(@PathVariable (value = "cnpj") String cnpj) throws IOException {
        final UserModel userLogado = userService.getUserByCnpj(cnpj);
        final String userType = userLogado.getType();

        final String userLogadoAddress = userLogado.getAddresses().get(0).getRua()
                + ", " + userLogado.getAddresses().get(0).getNumero()
                + " - " + userLogado.getAddresses().get(0).getBairro()
                + ", " + userLogado.getAddresses().get(0).getCidade()
                + ", " + userLogado.getAddresses().get(0).getEstado()
                + " - Brasil";

        final List<UserModel> userModelList = getUserService().getAllUsers();

        Map<String, String> userDistances = new HashMap<>();

        for (UserModel user : userModelList)
        {
            if (userType.equals(user.getType()))
            {
                continue;
            }

            String userAddress = user.getAddresses().get(0).getRua()
                    + ", " + user.getAddresses().get(0).getNumero()
                    + " - " + user.getAddresses().get(0).getBairro()
                    + ", " + user.getAddresses().get(0).getCidade()
                    + ", " + user.getAddresses().get(0).getEstado()
                    + " - Brasil";

            LocationDto distance =
                    getLocationService().getDistanceBetweenOriginAndDestiny(userLogadoAddress, userAddress);

            userDistances.put(user.getCnpj(), distance.getRows().get(0).getElements().get(0).getDistance().getText());

        }

        return ResponseEntity.ok(userDistances);
    }

    public UserService getUserService() {
        return userService;
    }

    public LocationService getLocationService() {
        return locationService;
    }

}
