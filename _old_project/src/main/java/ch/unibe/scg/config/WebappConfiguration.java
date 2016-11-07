package ch.unibe.scg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Just the import of the webapp-config.xml of spring-batch-admin-manager.
 */
@Configuration
@ImportResource("classpath:/org/springframework/batch/admin/web/resources/webapp-config.xml")
public class WebappConfiguration {

}