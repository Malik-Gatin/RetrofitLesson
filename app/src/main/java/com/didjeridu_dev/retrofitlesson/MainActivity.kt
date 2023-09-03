package com.didjeridu_dev.retrofitlesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.didjeridu_dev.retrofitlesson.adapter.ProductAdapter
import com.didjeridu_dev.retrofitlesson.databinding.ActivityMainBinding
import com.didjeridu_dev.retrofitlesson.retrofit.AuthRequest
import com.didjeridu_dev.retrofitlesson.retrofit.CartApi
import com.didjeridu_dev.retrofitlesson.retrofit.MainApi
import com.didjeridu_dev.retrofitlesson.retrofit.User
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Гость"
        adapter = ProductAdapter()
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val mainApi = retrofit.create(MainApi::class.java)

        var user: Response<User>? = null

        CoroutineScope(Dispatchers.IO).launch {
            user = mainApi.auth(
                AuthRequest(
                    username = "kminchelle",
                    password = "0lelplR"
                )
            )
            runOnUiThread{
                supportActionBar?.title = user?.body()?.firstName
            }
        }


        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {whenSearchAuth(query,mainApi, user?.body())}
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {whenSearchAuth(newText,mainApi, user?.body())}
                return true
            }

        })

        /*CoroutineScope(Dispatchers.IO).launch {
            val products = mainApi.getAllProducts()
            runOnUiThread{
                binding.apply {
                    adapter.submitList(products.products)
                }
            }
        }*/

        /*val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()*/

        //val mainApi = retrofit.create(MainApi::class.java)
        val cartApi = retrofit.create(CartApi::class.java)

        /*
        binding.button.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val user = mainApi.auth(
                    AuthRequest(
                        username = binding.username.text.toString(),
                        password = binding.password.text.toString()
                    )
                )
                runOnUiThread(){
                    binding.apply{
                        Picasso.get().load(user.image).into(iv)
                        firstName.text = user.username
                        lastName.text = user.lastName
                    }
                }
            }
        }

        binding.buttonCart.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val cart = cartApi.getCartById(3)
                runOnUiThread(){
                    binding.tvCart.text = cart.products[4].toString()
                }
            }
        }*/

    }

    fun whenSearchAuth(text: String, mainApi:MainApi, user:User?){
        CoroutineScope(Dispatchers.IO).launch {
            val products = mainApi.getProductsByNameAuth(user!!.token,"$text")
            runOnUiThread{
                binding.apply {
                    adapter.submitList(products.products)
                }
            }
        }
    }
}