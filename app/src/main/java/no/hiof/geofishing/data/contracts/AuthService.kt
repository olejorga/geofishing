package no.hiof.geofishing.data.contracts

interface AuthService {
    val authenticated: Boolean?
    val email: String?

    suspend fun login(email: String, password: String): Response<Unit>
    suspend fun logout(): Response<Unit>
    suspend fun signup(email: String, password: String, name: String): Response<Unit>
    suspend fun changePassword(newPassword: String, oldPassword: String): Response<Unit>
    suspend fun changeEmail(newEmail: String, password: String): Response<Unit>
}