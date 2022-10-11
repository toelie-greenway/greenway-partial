package greenway_myanmar.org.common.domain.entities

enum class AuthState {
  UNKNOWN {
    override fun isLoggedIn(): Boolean = false
    override fun isAuthenticated(): Boolean = false
  },
  LOGGED_IN {
    override fun isLoggedIn(): Boolean = true
    override fun isAuthenticated(): Boolean = true
  },
  LOGGED_OUT {
    override fun isLoggedIn(): Boolean = false
    override fun isAuthenticated(): Boolean = false
  },
  GUEST {
    override fun isLoggedIn(): Boolean = false
    override fun isAuthenticated(): Boolean = true
  };

  abstract fun isLoggedIn(): Boolean
  abstract fun isAuthenticated(): Boolean
}
