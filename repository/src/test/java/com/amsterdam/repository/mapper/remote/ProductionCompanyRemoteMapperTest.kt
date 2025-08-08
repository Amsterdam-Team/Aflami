import com.amsterdam.entity.ProductionCompany
import com.amsterdam.repository.dto.remote.ProductionCompanyDto
import com.amsterdam.repository.mapper.remote.toEntity
import com.amsterdam.repository.mapper.remote.toEntityList
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ProductionCompanyRemoteMapperTest {

    @Test
    fun `given ProductionCompanyDto, when toEntity called, then return correct ProductionCompany`() {
        // Given
        val dto = ProductionCompanyDto(
            id = 1,
            logoPath = "logo.png",
            name = "DreamWorks",
            originCountry = "US"
        )

        // When
        val result: ProductionCompany = dto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(1)
        assertThat(result.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500logo.png")
        assertThat(result.name).isEqualTo("DreamWorks")
        assertThat(result.country).isEqualTo("US")
    }

    @Test
    fun `given list of ProductionCompanyDto, when toEntityList called, then return list of ProductionCompany`() {
        // Given
        val dtoList = listOf(
            ProductionCompanyDto(1, "logo1.png", "Pixar", "US"),
            ProductionCompanyDto(2, null, "Marvel", "US")
        )

        // When
        val entityList = dtoList.toEntityList()

        // Then
        assertThat(entityList).hasSize(2)
        assertThat(entityList[0].name).isEqualTo("Pixar")
        assertThat(entityList[1].imageUrl).isEqualTo("")
        assertThat(entityList[1].name).isEqualTo("Marvel")
    }
}
