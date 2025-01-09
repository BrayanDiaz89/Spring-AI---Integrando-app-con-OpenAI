package com.aluracursos.ecomart.controllers;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorizador")
public class CategorizadorDeProductosController {

/* Configuración manual de ChatClient para cada controller
    private final ChatClient chatClient;

    public CategorizadorDeProductosController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptionsBuilder
                        .builder()
                        .withModel("gpt-4o-mini") //Puedo configurar el modelo a utilizar en properties, y también en el metodo, priorizará el metodo.
                        .build())
                .build();
    }
 */
//Configuración llamando a clase ChatClient de configurations, por medio de anotación @Qualifier,
//para no tener que crear el código anterior en cada controller.
    private final ChatClient chatClient;
    public CategorizadorDeProductosController(@Qualifier("gpt-4o-mini") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    public String categorizarProductos(String producto) {
        //Instrucciones al sistema AI para que los resultados sean los más certeros posibles
        var system = """
                Actúa como un categorizador de productos y debes responder solo el nombre de la categoría del producto informado.
                
                Escoge una categoría de la siguiente lista:
                
                1. Higiene personal
                2. Electrónicos
                3. Deportes
                4. Hogar
                5. Otros
                
                Ejemplo de uso:
                
                Producto: Pelota de fútbol
                Respuesta: Deportes
                """;
        //Conteo de tokens, para conocer conocer qué modelo utilizar
        var tokens = contadorDeTokens(system,producto);
        System.out.println(tokens);
        //Implementación de la lógica, para la selección del modelo:
        if(tokens > 2000){
            var temperature = 0.5; //Disminuir la creatividad, y mejorar la claridez o contundencia de la respuesta
            return this.chatClient.prompt()
                    .system(system)
                    .user(producto)
                    .options(ChatOptionsBuilder.builder()
                            .withTemperature(temperature)
                            .withModel("gpt-4o").build()) //si deseo configurar el modelo aquí, puedo dar enter antes del .build, y escribir .withModel("gpt-4o-mini") por ejemplo
                    .call()
                    .content();
        }
        //Sino entonces el modelo a utilizar será gpt-4o-mini
        var temperature = 0.5; //Disminuir la creatividad, y mejorar la claridez o contundencia de la respuesta
        return this.chatClient.prompt()
                .system(system)
                .user(producto)
                .options(ChatOptionsBuilder.builder()
                        .withTemperature(temperature)
                        .withModel("gpt-4o-mini").build())
                .call()
                .content();
        }

    //Reto: realizarlo en una clase separada, para realizar el conteo de tokens, y llamarlo en cualquier controller.
    private int contadorDeTokens(String system, String user){
        var registry = Encodings.newDefaultEncodingRegistry();
        var encoding = registry.getEncodingForModel(ModelType.GPT_4O_MINI);
        return encoding.countTokens(system + user);
    }
}
