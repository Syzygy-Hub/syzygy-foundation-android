package com.syzygyhub.foundation.contracts.network

/**
 * Contract for executing HTTP requests.
 *
 * Implementations live outside Foundation (e.g. in a Core or Services
 * module). Foundation only defines the protocol so that higher-level modules
 * can depend on an abstraction rather than a concrete HTTP client.
 */
interface NetworkClientProtocol {
    /**
     * Executes [request] and returns the server's response.
     *
     * Throws on transport-level errors (e.g. network unavailable, timeout).
     * HTTP error status codes are surfaced via [NetworkResponse.statusCode],
     * not exceptions.
     */
    suspend fun execute(request: NetworkRequest): NetworkResponse
}
