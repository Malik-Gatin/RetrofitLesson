package com.didjeridu_dev.retrofitlesson

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.didjeridu_dev.retrofitlesson.databinding.ContentBaseBinding

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ContentBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ContentBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}