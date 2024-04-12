package kern.elasticsearchplayground

import co.elastic.clients.elasticsearch._types.GeoLocation
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.data.elasticsearch.core.query.CriteriaQuery
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Service
import java.util.*

@Document(indexName = "with_geo_location_index", createIndex = true)
class WithGeoLocation(@Id val id: UUID, val location: GeoPoint)

interface WithGeoLocationRepository : ElasticsearchRepository<WithGeoLocation, UUID>

@Service
class WithGeoLocationService(val repository: WithGeoLocationRepository, val operations: ElasticsearchOperations) {

    companion object {
        val pointWeAreLookingFor = GeoPoint(60.0, 60.0)
        val otherPoint = GeoPoint(70.0, 70.0)
    }

    fun seedSampleData() {
        repository.saveAll(
            listOf(
                WithGeoLocation(UUID.randomUUID(), pointWeAreLookingFor),
                WithGeoLocation(UUID.randomUUID(), otherPoint),
            )
        )
    }

    fun searchOnlyWithNativeQuery(): SearchHits<WithGeoLocation> {
        val nativeGeoDistance = GeoDistanceQuery.Builder().field("location").distance("1km")
            .location(GeoLocation.Builder().latlon(LatLonGeoLocation.Builder().lat(pointWeAreLookingFor.lat).lon(pointWeAreLookingFor.lon).build()).build())
            .build()
        val nq = NativeQueryBuilder().withQuery { q -> q.geoDistance(nativeGeoDistance) }.build()

        return operations.search(nq, WithGeoLocation::class.java)
    }

    fun searchWithNativeQueryMixedWithCriteriaQueryAndGeoLocation(): SearchHits<WithGeoLocation> {
        val cq = CriteriaQuery(Criteria("location").within(pointWeAreLookingFor, "1km"))
        val nq = NativeQueryBuilder().withQuery(cq).build()

        return operations.search(nq, WithGeoLocation::class.java)
    }
}

