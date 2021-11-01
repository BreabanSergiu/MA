package com.example.myapplication.activities
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.ApplicationUtils.ApplicationContainer
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ApparelCardBinding
import com.example.myapplication.repository.ApparelRepositoryInMemory
import com.example.myapplication.repository.Repository
import com.example.myapplication.domain.Apparel
import com.example.myapplication.domain.Size
import com.example.myapplication.logd
import com.example.myapplication.toast


class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    private var repositoryMock: Repository = ApparelRepositoryInMemory()
    private val applicationContainer = ApplicationContainer()
    private val apparelRepository :Repository = applicationContainer.getSingletonApparelRepository()!!
    private val adapter = MainAdapter()
    private val addApparelActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        logd("Add apparel response: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                val intentBundle = intent.extras
                if (intentBundle != null) {
                    val id = intentBundle.getInt(ModifyActivity.ID)
                    var name = intentBundle.getString(ModifyActivity.NAME)
                    if (name == null) {
                        name = "Unknown name"
                    }
                    val composition = intentBundle.getString(ModifyActivity.COMPOSITION)!!
                    val company = intentBundle.getString(ModifyActivity.COMPANY)!!
                    val size: Size = enumValueOf(intentBundle.getString(ModifyActivity.SIZE)!!)
                    val description = intentBundle.getString(ModifyActivity.DESCRIPTION)!!
                    val picture = intentBundle.getString(ModifyActivity.PICTURE)!!

                    adapter.addApparelInList(Apparel(id,picture,name,company,size,description,composition))
                }
            }
        }
    }

    private val modifyApparelActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        logd("Modify apparel response: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                val intentBundle = intent.extras
                if (intentBundle != null) {
                    val position = intentBundle.getInt(ModifyActivity.POSITION)
                    val id = intentBundle.getInt(ModifyActivity.ID)
                    var name = intentBundle.getString(ModifyActivity.NAME)
                    if (name == null) {
                        name = "Unknown name"
                    }
                    val composition = intentBundle.getString(ModifyActivity.COMPOSITION)!!
                    val company = intentBundle.getString(ModifyActivity.COMPANY)!!
                    val size: Size = enumValueOf(intentBundle.getString(ModifyActivity.SIZE)!!)
                    val description = intentBundle.getString(ModifyActivity.DESCRIPTION)!!
                    val picture = intentBundle.getString(ModifyActivity.PICTURE)!!
                    
                    adapter.modifyApparelInList(Apparel(id,picture,name,company,size,description,composition), position)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        val view = bindingMain.root
        setContentView(view)

        bindingMain.recyclerView.adapter = adapter

        adapter.setApparelList(
            repositoryMock.findAll()
        )

        bindingMain.imageAdd.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            addApparelActivityLauncher.launch(intent)
        }

    }

    inner class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {

        var apparels = mutableListOf<Apparel>()

        @SuppressLint("NotifyDataSetChanged")
        fun setApparelList(apparels: List<Apparel>) {
            this.apparels = apparels.toMutableList()
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            val binding = ApparelCardBinding.inflate(inflater, parent, false)
            return MainViewHolder(binding)
        }

        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val apparel = apparels[position]
            holder.binding.nameFromCard.text = "NAME: "+apparel.name
            holder.binding.sizeFromCard.text = "MARIME: "+apparel.size.toString()
            holder.binding.companyFromCard.text = "Company: "+ apparel.company

            holder.binding.imageFromCard.load(apparel.picture)

            holder.binding.cardId.setOnClickListener{
                logd("pressed on item to modify")
                val intent = Intent(this@MainActivity, ModifyActivity::class.java)
                intent.putExtra(ModifyActivity.POSITION,position)
                intent.putExtra(ModifyActivity.PICTURE,apparel.picture)
                intent.putExtra(ModifyActivity.ID,apparel.id) //PUNEM IN BUNDLE
                intent.putExtra(ModifyActivity.NAME,apparel.name)
                intent.putExtra(ModifyActivity.COMPANY,apparel.company)
                intent.putExtra(ModifyActivity.SIZE,apparel.size.toString())
                intent.putExtra(ModifyActivity.DESCRIPTION,apparel.description)
                intent.putExtra(ModifyActivity.COMPOSITION,apparel.composition)
                modifyApparelActivityLauncher.launch(intent)
            }

            holder.binding.buttonDelete.setOnClickListener{
                apparels.removeAt(position)
                apparelRepository.remove(position)
                notifyDataSetChanged()
                toast("Apparel has been deleted successfully!")

            }


        }

        override fun getItemCount(): Int {
            return apparels.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun modifyApparelInList(apparel: Apparel, position: Int) {
            this.apparels[position] = apparel
            notifyDataSetChanged()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun addApparelInList(apparel: Apparel) {
            this.apparels.add(apparel)
            notifyDataSetChanged()
        }
    }

    class MainViewHolder(val binding: ApparelCardBinding) : RecyclerView.ViewHolder(binding.root)
}
