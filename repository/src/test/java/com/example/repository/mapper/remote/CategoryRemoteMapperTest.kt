//package com.example.repository.mapper.remote
//
//import com.example.entity.Category
//import com.example.repository.mapper.remote.testFactory.createRemoteCategoryDto
//import com.google.common.truth.Truth.assertThat
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class CategoryRemoteMapperTest {
//
//    private lateinit var mapper: CategoryRemoteMapper
//
//    @BeforeEach
//    fun setUp() {
//        mapper = CategoryRemoteMapper()
//    }
//
//    @Test
//    fun `should return Category entity with empty imageUrl when mapped from RemoteCategoryDto`() {
//        // Arrange
//        val dto = createRemoteCategoryDto(
//            id = 10,
//            name = "Action"
//        )
//
//        // Act
//        val result: Category = mapper.toEntity(dto)
//
//        // Assert
//        assertThat(result.id).isEqualTo(10)
//        assertThat(result.name).isEqualTo("Action")
//        assertThat(result.imageUrl).isEqualTo("")
//    }
//}
