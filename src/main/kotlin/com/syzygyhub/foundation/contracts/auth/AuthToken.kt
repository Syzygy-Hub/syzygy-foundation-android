package com.syzygyhub.foundation.contracts.auth

import com.syzygyhub.foundation.primitives.time.SyzygyTimestamp

/**
 * An authentication credential issued by an identity provider.
 *
 * @property accessToken The bearer token used to authenticate API requests.
 * @property refreshToken An optional token used to obtain a new access token
 *   after expiry.
 * @property expiresAt The point in time at which [accessToken] becomes
 *   invalid. `null` means the token does not expire (or the expiry is
 *   unknown).
 */
data class AuthToken(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresAt: SyzygyTimestamp? = null,
) {
    /**
     * `true` if [expiresAt] is set and the current time is past that point.
     * Returns `false` when [expiresAt] is `null`.
     */
    val isExpired: Boolean get() = expiresAt?.let { it < SyzygyTimestamp.now() } ?: false
}
