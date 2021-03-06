package io.supabase.postgrest.http

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ExtractCountTest {

    @ParameterizedTest
    @MethodSource("testData")
    fun `should extract count`(testData: TestDataExtractCount) {
        val requestHeaders = mapOf("Prefer" to testData.prefersHeader)
        val responseHeaders = mapOf("content-range" to testData.contentRangeHeader)

        val count = extractCount(responseHeaders, requestHeaders)

        assertThat(count).isEqualTo(testData.expectedCount)
    }

    @Suppress("unused")
    private fun testData(): Stream<TestDataExtractCount> {
        return Stream.of(
                TestDataExtractCount(
                        prefersHeader = "count=exact",
                        contentRangeHeader = "1-2/3",
                        expectedCount = 3
                ),
                TestDataExtractCount(
                        prefersHeader = "count=planned",
                        contentRangeHeader = "1-2/4",
                        expectedCount = 4
                ),
                TestDataExtractCount(
                        prefersHeader = "count=estimated",
                        contentRangeHeader = "1-2/5",
                        expectedCount = 5
                ),
                TestDataExtractCount(
                        prefersHeader = "count=exact",
                        contentRangeHeader = "1-2",
                        expectedCount = null
                ),
                TestDataExtractCount(
                        prefersHeader = "",
                        contentRangeHeader = "1-2/3",
                        expectedCount = null
                ),
        )
    }


}

data class TestDataExtractCount(
        val prefersHeader: String,
        val contentRangeHeader: String,
        val expectedCount: Long?
)