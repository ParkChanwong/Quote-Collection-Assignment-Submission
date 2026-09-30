package com.quotehunters.quotecollection.global.security.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenApiCustomizer optionalPageParameter() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }

            // Swagger에서는 정렬 입력란을 숨기고 서버의 기본 정렬을 사용한다.
            openApi.getPaths().values().forEach(path ->
                    path.readOperations().forEach(operation -> {
                        if (operation.getParameters() != null) {
                            operation.setParameters(operation.getParameters().stream()
                                    .filter(parameter -> !("sort".equals(parameter.getName())
                                            && "query".equals(parameter.getIn())))
                                    .toList());
                        }
                    }));

            // page 유무로 전체 조회와 페이지 조회를 구분하는 API에만 적용한다.
            for (String url : new String[]{
                    "/country", "/theme", "/period", "/field", "/person", "/quote"
            }) {
                var path = openApi.getPaths().get(url);
                if (path == null || path.getGet() == null
                        || path.getGet().getParameters() == null) {
                    continue;
                }

                for (var parameter : path.getGet().getParameters()) {
                    if ("page".equals(parameter.getName())
                            && "query".equals(parameter.getIn())) {
                        // 실제 요청 조건은 그대로 두고 Swagger에서만 선택 입력으로 표시한다.
                        parameter.setRequired(false);
                        parameter.setDescription("페이지 번호 (1부터 시작). 전체 조회는 비워두세요. size는 page를 입력한 경우에만 적용됩니다.");
                        if (parameter.getSchema() != null) {
                            // 자동 입력되는 기본값을 없애 page 없이 요청할 수 있게 한다.
                            parameter.getSchema().setDefault(null);
                        }
                    }
                }
            }
        };
    }

    @Bean
    public SwaggerIndexTransformer swaggerIndexTransformer(
            SwaggerUiConfigProperties config,
            SwaggerUiOAuthProperties oauth,
            SwaggerWelcomeCommon welcome,
            ObjectMapperProvider objectMapperProvider
    ) {
        return new SwaggerIndexPageTransformer(config, oauth, welcome, objectMapperProvider) {
            @Override
            public Resource transform(HttpServletRequest request, Resource resource,
                    ResourceTransformerChain chain) throws IOException {
                Resource transformed = super.transform(request, resource, chain);
                if (!"swagger-initializer.js".equals(resource.getFilename())) {
                    return transformed;
                }

                String script;
                try (var input = transformed.getInputStream()) {
                    script = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                }
                // YAML은 함수가 아닌 문자열로 전달되므로 UI 초기화 시 정렬 함수를 주입한다.
                script = script.replace("dom_id:", """
                        operationsSorter: (a, b) => {
                          const order = { get: 0, post: 1, put: 2, delete: 3, patch: 4, head: 5, options: 6, trace: 7 };
                          return ((order[a.get("method")] ?? 99) - (order[b.get("method")] ?? 99))
                            || a.get("path").localeCompare(b.get("path"));
                        },
                        dom_id:
                        """);
                return new TransformedResource(transformed, script.getBytes(StandardCharsets.UTF_8));
            }
        };
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("명언 도감 API")
                        .version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
