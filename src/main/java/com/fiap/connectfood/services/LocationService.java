package com.fiap.connectfood.services;

import com.fiap.connectfood.dto.LocationDto;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LocationService {

    public LocationDto getDistanceBetweenOriginAndDestiny(String origem, String destino) throws IOException {

        OkHttpClient client = new OkHttpClient();

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?" +
                "origins=" + origem +
                "&destinations=" + destino +
                "&units=metros" +
                "&key=AIzaSyDjH4Uuychteifj4QnTzJKChYAt8Ihc0to";

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return new Gson().fromJson(response.body().string(), LocationDto.class);
    }
}
