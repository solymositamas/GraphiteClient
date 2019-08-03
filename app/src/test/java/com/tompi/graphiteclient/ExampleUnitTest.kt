package com.tompi.graphiteclient

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val target = "apps.fakesite.*.counters.requests.count"
        val result = "summarize(apps.fakesite.web_server_02.counters.requests.count, \"1hour\", \"last\")"

        println("${getTargetName(target, result)}")
    }

    fun getTargetName(target: String, result: String): String {
        val idx = target.split(".").indexOf("*")
        return result.split(".").get(idx)
    }
}
