package com.didjeridu_dev.retrofitlesson.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.didjeridu_dev.retrofitlesson.R
import com.didjeridu_dev.retrofitlesson.databinding.ListItemBinding
import com.didjeridu_dev.retrofitlesson.retrofit.Product

class ProductAdapter : ListAdapter<Product, ProductAdapter.Holder>(Comparator()) {

    class Holder(view: View):RecyclerView.ViewHolder(view){
        private val binding = ListItemBinding.bind(view)
        fun bind(product: Product) = with(binding) {
            title.text = product.title
            description.text = product.description
        }
    }

    class Comparator : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}