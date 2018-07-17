package daggerok.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import org.springframework.web.reactive.result.view.Rendering
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Flux
import java.time.Duration

//tag::content[]
@Controller
class Pages {

  @GetMapping("/")
  fun index(model: Model) = Rendering
      .view("index")
      .modelAttribute("messages", ReactiveDataDriverContextVariable(
          Flux.zip(
              Flux.just("thi", "is", "a", "dynamic", "flux", "model", "from", "reactive", "thymeleaf", "spring", "5", "webflux", "integration", "!"),
              Flux.interval(Duration.ofMillis(250))
          ).map { it.t1 }, 3))
      .build()
}

@Configuration
class WebFluxWeb {

  @Bean
  fun webRoutes() = router {

    ("/").nest {

      contentType(TEXT_HTML)

      mapOf(
          "/bootstrap" to "bootstrap",
          "/materialize" to "materialize"
      ).forEach { path, view ->

        val interval = Duration.ofMillis(123)
        val items = Flux.just("this", "is", "flux", "static", "model")
        val flux = Flux
            .zip(items, Flux.interval(interval))
            .map { it.t1 }

        GET(path) {
          ok().render(view, mapOf("messages" to flux))
        }
      }
    }

    resources("/**", ClassPathResource("static/"))
  }
}
//end::content[]
