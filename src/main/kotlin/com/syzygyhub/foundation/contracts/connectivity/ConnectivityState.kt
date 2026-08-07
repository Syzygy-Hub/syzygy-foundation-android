package com.syzygyhub.foundation.contracts.connectivity

/**
 * The current network reachability state observed by a [ConnectivityProvider].
 */
enum class ConnectivityState {
    /** A network interface is available and traffic can flow. */
    CONNECTED,

    /** No network interface is available. */
    DISCONNECTED,

    /** Connectivity has not yet been determined (e.g. at app start). */
    UNKNOWN,

    ;

    /** `true` when this state is [CONNECTED]. */
    val isConnected: Boolean get() = this == CONNECTED
}
