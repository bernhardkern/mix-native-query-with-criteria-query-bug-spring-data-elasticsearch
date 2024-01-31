package kern.elasticsearchplayground

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ItemRepository : ElasticsearchRepository<Item, Int> {

    fun findByName(name: String): List<Item>

    fun findByCategory(category: String): List<Item>

    fun findByPriceBetween(low: Double, high: Double): List<Item>
}