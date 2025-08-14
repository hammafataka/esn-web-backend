package dev.mfataka.esnzlin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;

@OpenAPIDefinition(
        info = @Info(title = "ESN WEB BACKEND"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

//    public SwaggerConfig(MappingJackson2HttpMessageConverter converter) {
//        var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
//        supportedMediaTypes.add(new MediaType("application", "octet-stream"));
//        converter.setSupportedMediaTypes(supportedMediaTypes);
//    }

}
