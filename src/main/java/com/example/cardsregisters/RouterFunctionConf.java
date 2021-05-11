package com.example.cardsregisters;

import com.example.cardsregisters.dto.Card;
import com.example.cardsregisters.services.CardServices;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Card API", version = "1.0", description = "Documentation Card API v1.0"))
public class RouterFunctionConf {

    @RouterOperations({
            @RouterOperation(path = "/cards", beanClass = CardServices.class, beanMethod = "getAll", operation = @Operation(operationId = "getAll", description = "Obtener las cards", method = "GET")),
            @RouterOperation(path = "/cards/{idCard}", beanClass = CardServices.class, beanMethod = "getById", operation = @Operation(operationId = "getById", description = "Obtener las cards", method = "GET")),
            @RouterOperation(path = "/cards", beanClass = CardServices.class, beanMethod = "save", operation = @Operation(operationId = "save", description = "Obtener las cards", method = "POST")),
            @RouterOperation(path = "/createPerson", beanClass = CardServices.class, beanMethod = "update", operation = @Operation(operationId = "update", description = "Obtener las cards", method = "PUT")),
            @RouterOperation(path = "/cards/{idCard}", beanClass = CardServices.class, beanMethod = "delete", operation = @Operation(operationId = "delete", description = "Obtener las cards", method = "DELETE")) })
    @Bean
    public RouterFunction<ServerResponse> routes(final CardServices service) {
        return route(GET("/cards"), service::getCards)
                .andRoute(GET("/cards/{idCard}"), service::getCardById)
                .andRoute(POST("/cards"), service::saveCard)
                .andRoute(DELETE("/cards/{idCard}"), service::deleteCard)
                .andRoute(PUT("/cards"), service::updateCard);
    }

}
