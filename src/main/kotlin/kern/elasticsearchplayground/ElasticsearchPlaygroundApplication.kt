package kern.elasticsearchplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ElasticsearchPlaygroundApplication

fun main(args: Array<String>) {
	runApplication<ElasticsearchPlaygroundApplication>(*args)
}
