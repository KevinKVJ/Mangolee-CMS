package org.mangolee.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class TokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object>
{
    @Autowired
    private TokenFilter tokenFilter;
    @Override
    public GatewayFilter apply(Object config)
    {
        return tokenFilter;
    }
}