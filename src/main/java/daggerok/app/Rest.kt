package daggerok.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.toMono

//tag::content[]
@Configuration
class WebFluxRest {

  @Bean
  fun restRoutes() = router {

    ("/").nest {

      contentType(APPLICATION_JSON_UTF8)

      GET("/api/**") {
        val path = it.uri().toURL().path
        val parts = path.split("/")
        val name = if (parts.isEmpty()) "buddy" else parts.last()
        val params = it.queryParams().toSingleValueMap().toMutableMap()
            .filterNot { it.value.isNullOrEmpty() or it.value.isNullOrBlank() }
        val result = if (params.isEmpty()) mapOf("hello" to name) else params
        val publisher = result.toMono()
        ok().body(publisher)
      }
    }
  }
}
//end::content[]
