package kern.elasticsearchplayground

import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration
import java.util.*

@SpringBootTest(classes = [TestElasticsearchPlaygroundApplication::class])
@Testcontainers
@ActiveProfiles("test")
class WithGeoLocationServiceTest {

    @Autowired
    private lateinit var withGeoLocationRepository: WithGeoLocationRepository

    @Autowired
    private lateinit var withGeoLocationService: WithGeoLocationService

    @BeforeEach
    fun setUp() {
        withGeoLocationRepository.deleteAll()
    }

    @Test
    fun searchOnlyWithNativeQuery() {
        withGeoLocationService.seedSampleData()

        val result = withGeoLocationService.searchOnlyWithNativeQuery()

        result shouldHaveSize 1
    }

    @Test
    fun searchWithNativeQueryMixedWithCriteriaQueryAndGeoLocation() {
        withGeoLocationService.seedSampleData()

        val result = withGeoLocationService.searchWithNativeQueryMixedWithCriteriaQueryAndGeoLocation()

        result shouldHaveSize 1
    }
}