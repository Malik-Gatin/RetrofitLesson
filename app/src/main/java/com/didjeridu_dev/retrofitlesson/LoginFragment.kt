package com.didjeridu_dev.retrofitlesson

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.didjeridu_dev.retrofitlesson.databinding.FragmentLoginBinding
import com.didjeridu_dev.retrofitlesson.retrofit.AuthRequest
import com.didjeridu_dev.retrofitlesson.retrofit.MainApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var mainApi: MainApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        binding.apply {
            bSignIn.setOnClickListener{
                auth(
                    AuthRequest(
                        tvLogin.text.toString(),
                        tvPassword.text.toString()
                    )
                )
            }
            bNext.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
            }
        }
    }

    private fun initRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mainApi = retrofit.create(MainApi::class.java)
    }

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mainApi.auth(authRequest)
            requireActivity().runOnUiThread{
                val message = response.errorBody()?.string()?.let { JSONObject(it).getString("message") }
                binding.tvError.text = message
                val user = response.body()
                if(user!=null){
                    Picasso.get().load(user.image).into(binding.ivAvatar)
                    binding.tvName.text = user.firstName
                    binding.bNext.visibility = View.VISIBLE
                    viewModel.token.value = user.token
                }
            }
        }
    }
}