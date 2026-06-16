package spring.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true")
@ConditionalOnMissingClass("org.springdoc.webmvc.ui.SwaggerWelcomeWebMvc")
public class SwaggerUiFallbackController {

    @GetMapping(value = {"/swagger-ui.html", "/swagger-ui/"}, produces = MediaType.TEXT_HTML_VALUE)
    public String swaggerUi() {
        return """
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Security Practice API</title>
                    <link rel="stylesheet" href="/webjars/swagger-ui/swagger-ui.css">
                    <style>
                        html, body { margin: 0; min-height: 100%; background: #ffffff; }
                    </style>
                </head>
                <body>
                    <div id="swagger-ui"></div>
                    <script src="/webjars/swagger-ui/swagger-ui-bundle.js"></script>
                    <script src="/webjars/swagger-ui/swagger-ui-standalone-preset.js"></script>
                    <script>
                        window.onload = function () {
                            window.ui = SwaggerUIBundle({
                                url: "/v3/api-docs",
                                dom_id: "#swagger-ui",
                                deepLinking: true,
                                displayRequestDuration: true,
                                persistAuthorization: true,
                                presets: [
                                    SwaggerUIBundle.presets.apis,
                                    SwaggerUIStandalonePreset
                                ],
                                plugins: [
                                    SwaggerUIBundle.plugins.DownloadUrl
                                ],
                                layout: "StandaloneLayout"
                            });
                        };
                    </script>
                </body>
                </html>
                """;
    }
}
