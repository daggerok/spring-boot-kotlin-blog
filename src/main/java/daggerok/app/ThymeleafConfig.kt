package daggerok.app

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.ViewResolverRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver

//tag::content[]
@Configuration
@EnableConfigurationProperties(ThymeleafProperties::class)
class ThymeleafConfig(val engine: ISpringWebFluxTemplateEngine) : WebFluxConfigurer {

  @Bean
  fun thymeleafChunkedAndDataDrivenViewResolver(): ThymeleafReactiveViewResolver {
    val viewResolver = ThymeleafReactiveViewResolver()
    viewResolver.responseMaxChunkSizeBytes = 8192
    viewResolver.templateEngine = engine
    viewResolver.order = 1
    return viewResolver
  }

  override fun configureViewResolvers(registry: ViewResolverRegistry) {
    registry.viewResolver(thymeleafChunkedAndDataDrivenViewResolver())
  }
}
//end::content[]
