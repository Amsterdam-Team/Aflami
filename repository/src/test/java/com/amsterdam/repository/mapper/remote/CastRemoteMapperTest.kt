//package com.amsterdam.repository.mapper.remote
//
//import com.amsterdam.entity.Gender
//import com.amsterdam.repository.mapper.remote.testFactory.createRemoteCastDto
//import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
//import com.google.common.truth.Truth.assertThat
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//
//class CastRemoteMapperTest {
//
//    private lateinit var mapper: CastRemoteMapper
//
//    @BeforeEach
//    fun setUp() {
//        mapper = CastRemoteMapper()
//    }
//
//    @Test
//    fun `should return Male when gender is 2`() {
//        val dto = createRemoteCastDto(gender = 2, profilePath = "/john.jpg")
//        val result = mapper.toEntity(dto)
//
//        assertThat(result.gender).isEqualTo(Gender.Male)
//    }
//
//    @Test
//    fun `should return Female when gender is not 2`() {
//        val dto = createRemoteCastDto(gender = 1, profilePath = "/jane.jpg")
//        val result = mapper.toEntity(dto)
//
//        assertThat(result.gender).isEqualTo(Gender.Female)
//    }
//
//    @Test
//    fun `should return correct imageUrl when profilePath is not null`() {
//        val dto = createRemoteCastDto(profilePath = "/actor.jpg")
//        val result = mapper.toEntity(dto)
//
//        assertThat(result.imageUrl).isEqualTo("$BASE_IMAGE_URL_W500/actor.jpg")
//    }
//
//    @Test
//    fun `should return empty imageUrl when profilePath is empty`() {
//        val dto = createRemoteCastDto(profilePath = "")
//        val result = mapper.toEntity(dto)
//
//        assertThat(result.imageUrl).isEqualTo(BASE_IMAGE_URL_W500)
//    }
//
//    @Test
//    fun `should map all fields correctly`() {
//        val dto = createRemoteCastDto(
//            id = 999,
//            name = "Test Actor",
//            gender = 2,
//            profilePath = "/test.jpg",
//            popularity = 123.45
//        )
//
//        val result = mapper.toEntity(dto)
//
//        assertThat(result.id).isEqualTo(999)
//        assertThat(result.name).isEqualTo("Test Actor")
//        assertThat(result.gender).isEqualTo(Gender.Male)
//        assertThat(result.imageUrl).isEqualTo("$BASE_IMAGE_URL_W500/test.jpg")
//        assertThat(result.popularity).isEqualTo(123.45)
//    }
//}
