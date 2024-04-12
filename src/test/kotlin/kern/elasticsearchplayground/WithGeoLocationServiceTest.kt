package kern.elasticsearchplayground

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

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

        result shouldHaveSize 3
    }

    @Test
    fun searchWithNativeQueryMixedWithCriteriaQueryAndGeoLocation() {
        withGeoLocationService.seedSampleData()

        val result = withGeoLocationService.searchWithNativeQueryMixedWithCriteriaQueryAndGeoLocation()

        result shouldHaveSize 3
    }

    @Test
    fun searchWithNativeQueryMixedWithCriteriaQueryAndGeoLocationAndAggregation() {
        withGeoLocationService.seedSampleData()

        val result = withGeoLocationService.searchWithNativeQueryMixedWithCriteriaQueryAndGeoLocationAndAggregation()

        result shouldHaveSize 3 //works with spring-data-elasticsearch 5.3.0-M2

        val foundAggregationIds = (result.aggregations as ElasticsearchAggregations)
            .aggregationsAsMap()["exampleAggregation"]
                ?.aggregation()
                ?.aggregate?.lterms()
                ?.buckets()?.array()
                ?.map { it.key() }?.toSet() ?: emptySet()

        foundAggregationIds shouldContainExactlyInAnyOrder listOf(1L, 2L)
    }
}