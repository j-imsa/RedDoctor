package be.jimsa.reddoctor.config.document;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.*;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = API_DOCUMENT_INFO_TITLE,
                description = API_DOCUMENT_INFO_DESCRIPTION,
                version = API_DOCUMENT_INFO_VERSION,
                contact = @Contact(
                        name = API_DOCUMENT_INFO_CONTACT_NAME,
                        url = API_DOCUMENT_INFO_CONTACT_URL,
                        email = API_DOCUMENT_INFO_CONTACT_EMAIL
                ),
                license = @License(
                        name = API_DOCUMENT_INFO_LICENSE_NAME,
                        url = API_DOCUMENT_INFO_LICENSE_URL
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = API_DOCUMENT_EXTERNAL_DOCS_DESCRIPTION,
                url = API_DOCUMENT_EXTERNAL_DOCS_URL
        ),
        servers = {
                @Server(
                        description = API_DOCUMENT_SERVERS_1_DESCRIPTION,
                        url = API_DOCUMENT_SERVERS_1_URL
                ),
                @Server(
                        description = API_DOCUMENT_SERVERS_2_DESCRIPTION,
                        url = API_DOCUMENT_SERVERS_2_URL
                )
        }
)
public class AppDocument {
}
