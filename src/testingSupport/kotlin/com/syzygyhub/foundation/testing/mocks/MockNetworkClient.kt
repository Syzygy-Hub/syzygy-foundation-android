package com.syzygyhub.foundation.testing.mocks

import com.syzygyhub.foundation.contracts.network.NetworkClientProtocol
import com.syzygyhub.foundation.contracts.network.NetworkRequest
import com.syzygyhub.foundation.contracts.network.NetworkResponse

/**
 * A [NetworkClientProtocol] implementation that returns pre-configured
 * responses or throws a pre-configured error.
 *
 * Enqueue [NetworkResponse] objects in [responses] before the code under
 * test calls [execute]. Each response is consumed in FIFO order.
 */
class MockNetworkClient : NetworkClientProtocol {
    /** Responses to return, consumed in FIFO order. */
    val responses: MutableList<NetworkResponse> = mutableListOf()

    /** All requests that have been executed, in order. */
    val requests: MutableList<NetworkRequest> = mutableListOf()

    /**
     * When non-null, [execute] throws this error instead of returning a
     * response.
     */
    var error: Throwable? = null

    override suspend fun execute(request: NetworkRequest): NetworkResponse {
        requests.add(request)
        error?.let { throw it }
        return responses.removeFirst()
    }

    override fun dispose() {}
}
