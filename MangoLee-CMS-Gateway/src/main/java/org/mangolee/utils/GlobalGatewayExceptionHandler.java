package org.mangolee.utils;

import lombok.Getter;
import lombok.Setter;
import org.mangolee.entity.Result;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GlobalGatewayExceptionHandler implements ErrorWebExceptionHandler {

    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
    private List<ViewResolver> viewResolvers = Collections.emptyList();
    private ThreadLocal<Result> threadLocal=new ThreadLocal<>();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        if(!exchange.getResponse().isCommitted())
        {
            Result result = Result.INTERNAL_ERROR;//TODO 未来对不同throwable类型返回不同error
            threadLocal.set(result);
            ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
            return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                    .switchIfEmpty(Mono.error(throwable))
                    .flatMap((handler) -> handler.handle(newRequest))
                    .flatMap((response) -> write(exchange, response));
        }
        else
        {
            return Mono.error(throwable);
        }
    }

    /**
     * 统一返回指定异常信息(指定json格式输出)
     * @param request
     * @return
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request){
        return ServerResponse.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(threadLocal.get()));
    }

    /**
     * 参考DefaultErrorWebExceptionHandler
     */
    private Mono<? extends Void> write(ServerWebExchange exchange, ServerResponse response) {
        exchange.getResponse().getHeaders().setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }


    /**
     * 参考DefaultErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {
        private ResponseContext() {
        }

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return GlobalGatewayExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return GlobalGatewayExceptionHandler.this.viewResolvers;
        }
    }
}

