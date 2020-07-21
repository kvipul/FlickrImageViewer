package com.sablania.flickrimageviewer.testUtils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.sablania.baseandroidlibrary.RetrofitProvider
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Vipul Kumar on 21/07/20.
 */
class BaseRule : TestRule {
    val context: Context = ApplicationProvider.getApplicationContext()
    lateinit var server: MockWebServer

    override fun apply(statement: Statement, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                // Here is BEFORE_CODE==============================================================

                server = MockWebServer()
                server.start()

                val mockRetrofit = Retrofit.Builder()
                    .baseUrl(server.url("/"))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                RetrofitProvider("/").setRetrofitInstance(mockRetrofit)

                try {
                    statement.evaluate()
                } finally {
                    // Here is AFTER_CODE
                    server.shutdown()
                }
            }
        }
    }
}