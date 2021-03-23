class TokenManager {
    companion object {
        var instance: TokenManager? = null
            get() {
                if (instance == null) instance = TokenManager()
                return instance
            }
    }


    var token = ""
}