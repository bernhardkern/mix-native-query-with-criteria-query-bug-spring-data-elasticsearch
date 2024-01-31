package kern.elasticsearchplayground

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.data.elasticsearch.core.query.CriteriaQuery
import org.springframework.data.elasticsearch.core.query.Query
import org.springframework.stereotype.Service


@Service
class ItemService(private val elasticsearchOperations: ElasticsearchOperations) {

    fun saveItem(item: Item): Int {
        val itemEntity = elasticsearchOperations.save(item)
        return itemEntity.id
    }

    fun saveItemsBulk(itemList: List<Item>): List<Int> = itemList.map(this::saveItem)


    fun deleteItem(item: Item): String = elasticsearchOperations.delete(item)


    fun searchWithCriteria(name: String): SearchHits<Item> {
        // Get all item with given name
        val criteria = Criteria("name").`is`(name)
        val searchQuery: Query = CriteriaQuery(criteria)
        return elasticsearchOperations.search(searchQuery, Item::class.java)
    }

    fun searchWithNativeQuery(name: String): SearchHits<Item> {
        val query = NativeQuery.builder().withQuery { qb -> qb.match { match -> match.field("name").query(name) } }.build()
        return elasticsearchOperations.search(query, Item::class.java)
    }
}