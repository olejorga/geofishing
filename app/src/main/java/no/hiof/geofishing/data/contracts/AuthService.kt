package no.hiof.geofishing.data.contracts

/**
 * Represents an auth-service with email & password authentication.
 *
 * @property authenticated A boolean that indicates whether a user is logged in or not.
 * @property email The email of the user, null if not authenticated.
 * @property id The id of the authenticated account.
 */
interface AuthService {
    val authenticated: Boolean
    val email: String?
    val id: String?

    /**
     * Asynchronously authenticate a user by email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A standardized response.
     */
    suspend fun login(email: String, password: String): Response<Unit>

    /**
     * Asynchronously revoke the user's authentication.
     */
    suspend fun logout(): Response<Unit>

    /**
     * Asynchronously create a user account.
     *
     * @param email The user's email.
     * @param password The desired password.
     * @param name: The user's name.
     * @return A standardized response.
     */
    suspend fun signup(email: String, password: String, name: String): Response<Unit>

    /**
     * Asynchronously change the user's password.
     *
     * @param newPassword The desired password.
     * @param oldPassword The user's previous password.
     * @return A standardized response.
     */
    suspend fun changePassword(newPassword: String, oldPassword: String): Response<Unit>

    /**
     * Asynchronously change the email associated with user's account.
     *
     * @param newEmail The desired email.
     * @param password The user's password.
     * @return A standardized response.
     */
    suspend fun changeEmail(newEmail: String, password: String): Response<Unit>
}