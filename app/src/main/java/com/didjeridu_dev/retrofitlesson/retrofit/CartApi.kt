package com.didjeridu_dev.retrofitlesson.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface CartApi {
    @GET("carts/{id}")
    suspend fun getCartById(@Path("id") id:Int): Cart
}