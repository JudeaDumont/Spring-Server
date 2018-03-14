package server.springcontext.internalresourcing

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class MvcConfig : WebMvcConfigurerAdapter() {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry?) {
        //This maps directories into the resource context (as opposed to the application context)
        //This means that to reference a file that would be pulled along with html per say
        //That you only reference that file with "/file.js"
        //Rather than naming any other part of the application context directory structure (js/file.js).
        registry!!
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/resources/static/")
                .addResourceLocations("classpath:/resources/templates/")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("classpath:/templates/")
                .addResourceLocations("classpath:/webapp/")
    }

    override fun configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer?) {
        configurer!!.enable()
    }

    //potential for cross origin requests

//    override fun addCorsMappings(registry: CorsRegistry?) {
//        registry!!.addMapping("/**")
//                .allowedOrigins("http://localhost:63342/")
//                .allowCredentials(false)
//                .maxAge(3600)
//                .allowedHeaders("Accept", "Content-Type", "Origin",
//                        "Authorization", "X-Auth-Token")
//                .exposedHeaders("X-Auth-Token", "Authorization")
//                .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS")
//    }
}