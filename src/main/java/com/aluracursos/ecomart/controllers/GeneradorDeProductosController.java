package com.aluracursos.ecomart.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generador")
public class GeneradorDeProductosController {

/* Configuración manual de ChatClient para cada controller

    private final ChatClient chatClient;

    public GeneradorDeProductosController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptionsBuilder
                        .builder()
                        .withModel("gpt-4o-mini") //Puedo configurar el modelo a utilizar en app.properties, y también en el metodo, priorizará el metodo.
                        .build())
                .build();
    }
 */
//Configuración llamando a clase ChatClient de configurations y su metodo, por medio de anotación @Qualifier,
//para no tener que crear el código anterior en cada controller.
    private final ChatClient chatClient;

    public GeneradorDeProductosController(@Qualifier("gpt-4o-mini") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    public String generadorDeProductos() {
        var pregunta = "Genera 5 productos ecológicos";
        return this.chatClient.prompt()
                .user(pregunta)
                .call()
                .content();
    }
}
