package dev.ihorshulha.publicapi.filter;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

@Component
@ControllerAdvice
public class TraceIdResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response) {
        SpanContext ctx = Span.current().getSpanContext();

        if (ctx.isValid() && body != null) {
            if (body instanceof dev.ihorshulha.publicapi.model.Event event) {
                Map<String, Object> result = new HashMap<>();
                result.put("uid", event.uid());
                result.put("subject", event.subject());
                result.put("description", event.description());
                result.put("traceId", ctx.getTraceId());
                return result;
            }
        }
        return body;
    }
}
