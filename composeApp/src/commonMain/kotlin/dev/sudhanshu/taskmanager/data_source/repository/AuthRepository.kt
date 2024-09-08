package dev.sudhanshu.taskmanager.data_source.repository



import com.taskapp.database.LoginStateQueries
import com.taskapp.database.UserQueries
import dev.sudhanshu.taskmanager.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AuthRepository(
    private val database: AppDatabase
) {



    private val userQueries: UserQueries = database.userQueries
    private val loginStateQueries: LoginStateQueries = database.loginStateQueries


    fun registerUser(email: String, password: String): Boolean {
        return try {
            userQueries.deleteData()
            userQueries.insertUser(email, password)
            val user = userQueries.getUserByEmail(email).executeAsOneOrNull()
            if (user?.password == password) {
                user.id.let { userId ->
                    loginStateQueries.insertLoginState(userId)
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            println("--AuthViewModel--, Error registering user: ${e.message}")
            false
        }
    }


    fun loginUser(email: String, password: String): Boolean {
        val user = userQueries.getUserByEmail(email).executeAsOneOrNull()
        return if (user?.password == password) {
            user.id.let { userId ->
                loginStateQueries.insertLoginState(userId)
            }
            true
        } else {
            false
        }
    }

    fun isLoggedIn(): Boolean {
        return try {
            loginStateQueries.selectAll().executeAsOneOrNull()?.userId?.toInt() != null
        } catch (e: Exception) {
            false
        }
    }
    fun logout() {
        loginStateQueries.deleteAll()
    }
    fun getUserEmail(): Flow<String> = flow {
        val email = userQueries.getUserEmail().executeAsOneOrNull()
        emit(email ?: "No Email")
    }

}

