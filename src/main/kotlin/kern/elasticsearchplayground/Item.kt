package kern.elasticsearchplayground

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.FieldType.Keyword
import org.springframework.data.elasticsearch.annotations.FieldType.Text

@Document(indexName = "itemindex")
class Item(
    @Id
    val id: Int,

    @Field(type = Text, name = "name")
    val name: String,

    @Field(type = FieldType.Double, name = "price")
    val price: Double,

    @Field(type = Keyword, name = "brand")
    val brand: String,

    @Field(type = Keyword, name = "category")
    val category: String,
)