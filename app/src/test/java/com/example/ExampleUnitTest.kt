package com.example

import com.example.core.result.AppResult
import com.example.core.result.fold
import com.example.core.result.getOrElse
import com.example.core.result.getOrNull
import com.example.core.result.map
import com.example.data.remote.dto.ExampleDto
import com.example.domain.model.ExampleItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  private val json = Json { ignoreUnknownKeys = true }

  @Test
  fun `test AppResult fold and success mapping`() {
    val successResult: AppResult<String> = AppResult.Success("Clean Architecture")
    val upper = successResult.map { it.uppercase() }

    assertEquals("CLEAN ARCHITECTURE", upper.getOrNull())
    assertTrue(upper.isSuccess)

    val folded = upper.fold(
      onSuccess = { "Handled: $it" },
      onError = { "Error: ${it.message}" }
    )
    assertEquals("Handled: CLEAN ARCHITECTURE", folded)
  }

  @Test
  fun `test AppResult error handling`() {
    val errorResult: AppResult<String> = AppResult.Error(IllegalArgumentException("Invalid payload"))
    assertTrue(errorResult.isError)
    assertNull(errorResult.getOrNull())
    assertEquals("Fallback", errorResult.getOrElse("Fallback"))
  }

  @Test
  fun `test ExampleDto kotlinx serialization roundtrip`() {
    val item = ExampleItem(
      id = "1",
      title = "Clean Architecture + MVI",
      description = "Unidirectional Data Flow starter"
    )

    val dto = ExampleDto.fromDomain(item)
    val jsonString = json.encodeToString(dto)

    assertTrue(jsonString.contains("Clean Architecture + MVI"))

    val decoded: ExampleDto = json.decodeFromString(jsonString)
    val domainItem = decoded.toDomain()

    assertEquals(item.id, domainItem.id)
    assertEquals(item.title, domainItem.title)
  }
}
