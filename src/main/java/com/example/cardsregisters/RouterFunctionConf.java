package com.example.cardsregisters;

import com.example.cardsregisters.services.CardServices;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Card API", version = "1.0", description = "Documentation Card API v1.0"))
public class RouterFunctionConf {

    @RouterOperations({
            @RouterOperation(path = "/cards", method = RequestMethod.GET, beanClass = CardServices.class, beanMethod = "getCards"),
            @RouterOperation(path = "/cards/{idCard}",  method = RequestMethod.GET, beanClass = CardServices.class, beanMethod = "getCardById"),
            @RouterOperation(path = "/cards",  method = RequestMethod.POST, beanClass = CardServices.class, beanMethod = "saveCard"),
            @RouterOperation(path = "/cards/{idCard}",  method = RequestMethod.DELETE, beanClass = CardServices.class, beanMethod = "deleteCard"),
            @RouterOperation(path = "/cards",  method = RequestMethod.PUT, beanClass = CardServices.class, beanMethod = "updateCard")
    })
    @Bean
    public RouterFunction<ServerResponse> routes(final CardServices service) {
        return route(GET("/cards"), service::getCards)
                .andRoute(GET("/cards/{idCard}"), service::getCardById)
                .andRoute(POST("/cards"), service::saveCard)
                .andRoute(DELETE("/cards/{idCard}"), service::deleteCard)
                .andRoute(PUT("/cards"), service::updateCard);
    }

}
