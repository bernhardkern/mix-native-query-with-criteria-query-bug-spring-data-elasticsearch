package kern.elasticsearchplayground

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration

@TestConfiguration(proxyBeanMethods = false)
@EnableElasticsearchRepositories(basePackages = ["kern.elasticsearchplayground"])
class TestElasticsearchPlaygroundApplication {

	@Bean
	@ServiceConnection
	fun elasticsearchContainer(): ElasticsearchContainer {
		return ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.12.0"))
			.withEnv("discovery.type", "single-node")
			.withEnv("xpack.security.enabled", "false")
			.withEnv("ES_JAVA_OPTS", "-Xms2g -Xmx4g")
			.apply {
				setWaitStrategy(HostPortWaitStrategy().withStartupTimeout(Duration.ofSeconds(180)))
			}

	}
}

fun main(args: Array<String>) {
	fromApplication<ElasticsearchPlaygroundApplication>().with(TestElasticsearchPlaygroundApplication::class).run(*args)
}
