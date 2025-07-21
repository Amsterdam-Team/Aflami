package com.example.remotedatasource

import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.datasource.CountryRemoteDataSourceImpl
import com.example.repository.dto.remote.RemoteCountryDto
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CountryRemoteDataSourceImplTest {
    private lateinit var networkClient: NetworkClient
    private lateinit var countryRemoteDataSourceImpl: CountryRemoteDataSourceImpl
    private lateinit var httpResponse: HttpResponse

    private val jsonSerializer =
        Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        networkClient = mockk()
        httpResponse = mockk()
        countryRemoteDataSourceImpl = CountryRemoteDataSourceImpl(networkClient)
    }

    @Test
    fun `getCountries should return a list of countries when executed`() = runTest {
        //Given
        val jsonString = """
    [
      {"iso_3166_1": "EG", "english_name": "Egypt", "native_name": "مصر"},
      {"iso_3166_1": "US", "english_name": "United States", "native_name": "United States"}
    ]
""".trimIndent()

        val expectedCountriesDtoList =
            jsonSerializer.decodeFromString<List<RemoteCountryDto>>(jsonString)

        val mockHttpResponse = mockk<HttpResponse>(relaxed = true)

        coEvery { mockHttpResponse.status } returns HttpStatusCode.OK

        coEvery { mockHttpResponse.body<List<RemoteCountryDto>>() } returns expectedCountriesDtoList

        coEvery {
            networkClient.get("configuration/countries", any())
        } returns mockHttpResponse

        //When
        val countries = countryRemoteDataSourceImpl.getCountries()

        //Then
        assertEquals("EG", countries[0].isoCode)
        assertEquals("United States", countries[1].englishName)
        assertEquals(2, countries.size)
    }

}