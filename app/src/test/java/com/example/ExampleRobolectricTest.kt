package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Family Tree", appName)
  }

  @Test
  fun `verify application container initialization`() {
    val context = ApplicationProvider.getApplicationContext<ArchBaseApplication>()
    assertNotNull(context.container)
    assertNotNull(context.container.exampleRepository)
    assertNotNull(context.container.preferencesDataSource)
    assertNotNull(context.container.getExampleItemsUseCase)
  }
}
