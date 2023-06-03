package com.fiap.connectfood.controller;

import com.fiap.connectfood.dto.LocationDto;
import com.fiap.connectfood.services.LocationService;
import com.squareup.okhttp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    LocationService locationService;

    @GetMapping("/calculate-distance")
    public ResponseEntity<LocationDto> calculateDistance(@RequestParam (value = "origem") String origem,
                                                        @RequestParam (value = "destino") String destino) throws IOException {

        LocationDto locationDto = locationService.getDistanceBetweenOriginAndDestiny(origem, destino);

        // Consultar metodo que tras todos os usuarios
        // realizar logica para calcular a distancia do usuario logado, com todos os usuarios
        // Fazer sort, ou seja ordenarar do mais perto ao mais longe
        // Talvez tenha que criar um campo temporario para salvar a distancia dos usuarios, para dps realizar
        // a logica da ordenação

        return ResponseEntity.ok(locationDto);
    }
}
