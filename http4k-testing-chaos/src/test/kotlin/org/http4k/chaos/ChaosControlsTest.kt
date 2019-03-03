package org.http4k.chaos

import com.natpryce.hamkrest.and
<<<<<<< HEAD
import org.http4k.chaos.ChaosBehaviours.ReturnStatus
import com.natpryce.hamkrest.assertion.assertThat

import com.natpryce.hamkrest.should.shouldMatch
=======
import com.natpryce.hamkrest.assertion.assertThat
>>>>>>> replace shouldMatch with assertThat
import kotlinx.coroutines.runBlocking
import org.http4k.chaos.ChaosStages.Wait
import org.http4k.chaos.ChaosTriggers.Always
import org.http4k.contract.ApiKey
import org.http4k.contract.NoSecurity
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.I_M_A_TEAPOT
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.http4k.lens.Header
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.junit.jupiter.api.Test

class ChaosControlsTest {

    private val noChaos = """{"chaos":"none"}"""
    private val originalChaos = """{"chaos":"Always ReturnStatus (404)"}"""
    private val customChaos = """{"chaos":"Always ReturnStatus (418)"}"""

    @Test
    fun `can convert a normal app to be chaotic`() = runBlocking {
        val app = routes("/" bind GET to { Response(OK) })

        val appWithChaos = app.withChaosControls(ReturnStatus(NOT_FOUND).appliedWhen(Always()))

        assertThat(appWithChaos(Request(GET, "/chaos/status")), hasBody(noChaos))
        assertThat(appWithChaos(Request(POST, "/chaos/activate")), hasStatus(OK).and(hasBody(originalChaos)))
        assertThat(appWithChaos(Request(GET, "/chaos/status")), hasBody(originalChaos))
        assertThat(appWithChaos(Request(POST, "/")), hasStatus(NOT_FOUND))
        assertThat(appWithChaos(Request(GET, "/")), hasStatus(NOT_FOUND))
        assertThat(appWithChaos(Request(POST, "/chaos/deactivate")), hasStatus(OK).and(hasBody(noChaos)))
        assertThat(appWithChaos(Request(GET, "/chaos/status")), hasBody(noChaos))
        assertThat(appWithChaos(Request(GET, "/")), hasStatus(OK))
        assertThat(appWithChaos(Request(POST, "/chaos/activate/new").body("""
                   [{
                       "type":"trigger",
                       "trigger": {
                           "type":"always"
                       },
                       "behaviour":{
                           "type":"status",
                           "status":418
                       }
                   }]""".trimIndent())), hasStatus(OK).and(hasBody(customChaos)))
        assertThat(appWithChaos(Request(GET, "/chaos/status")), hasBody(customChaos))
        assertThat(appWithChaos(Request(GET, "/")), hasStatus(I_M_A_TEAPOT))
        assertThat(appWithChaos(Request(POST, "/chaos/deactivate")), hasStatus(OK).and(hasBody(noChaos)))
        assertThat(appWithChaos(Request(GET, "/chaos/status")), hasBody(noChaos))
        assertThat(appWithChaos(Request(POST, "/chaos/activate")), hasStatus(OK).and(hasBody(customChaos)))
<<<<<<< HEAD
=======
        Unit
>>>>>>> replace shouldMatch with assertThat
    }

    @Test
    fun `can configure chaos controls`() = runBlocking {
        val app = routes("/" bind GET to { Response(OK) })

        val appWithChaos = app.withChaosControls(
                Wait,
                ApiKey(Header.required("secret"), { true }),
                "/context"
        )

        assertThat(appWithChaos(Request(GET, "/context/status")), hasStatus(UNAUTHORIZED))
        assertThat(appWithChaos(Request(GET, "/context/status").header("secret", "whatever")), hasStatus(OK))
<<<<<<< HEAD
=======
        Unit
>>>>>>> replace shouldMatch with assertThat
    }

    @Test
    fun `combines with other route blocks`() = runBlocking {
        val app = routes("/{bib}/{bar}" bind GET to { Response(I_M_A_TEAPOT).body(it.path("bib")!! + it.path("bar")!!) })

        val appWithChaos = app.withChaosControls(
                Wait,
                NoSecurity,
                "/context"
        )

        assertThat(appWithChaos(Request(GET, "/context/status")), hasStatus(OK))
        assertThat(appWithChaos(Request(GET, "/foo/bob")), hasStatus(I_M_A_TEAPOT).and(hasBody("foobob")))
<<<<<<< HEAD
=======
        Unit
>>>>>>> replace shouldMatch with assertThat
    }
}