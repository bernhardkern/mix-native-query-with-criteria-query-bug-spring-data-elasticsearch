package kern.elasticsearchplayground

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class SeedConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun seed(itemRepository: ItemRepository) = ApplicationRunner {
        if (itemRepository.count() == 0L) {
            val data = ClassPathResource("items.csv").file.readLines(Charsets.UTF_8).drop(1)
            itemRepository.saveAll(data.map {
                val splitted = it.split(",")
                Item(id = splitted[0].toInt(), name = splitted[1], price = splitted[2].toDouble(), brand = splitted[3], category = splitted[4])
            }).also { log.info("seeded ${it.count()} items") }
        }
    }
}