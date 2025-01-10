package com.aluracursos.ecomart.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imagen")
public class GeneradorDeImagenesController {

    private final ImageModel imageModel;

    public GeneradorDeImagenesController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping
    public String generadorDeImagenes(String prompt) {
        var options = ImageOptionsBuilder.builder()
                .withHeight(1024) //Modificar las opciones por defecto
                .withWidth(1024)
                .build();
        var response = imageModel.call(new ImagePrompt(prompt, options));
        return response.getResult().getOutput().getUrl();
    }
}
