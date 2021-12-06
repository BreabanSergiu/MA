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
import com.example.myapplication.repository.db.ApparelContract
import com.example.myapplication.repository.db.ApparelDbManager
import com.example.myapplication.toast


class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    private var repositoryMock: Repository = ApparelRepositoryInMemory()
    private val applicationContainer = ApplicationContainer()
    private val apparelRepository: Repository =
        applicationContainer.getSingletonApparelRepository()!!
    private val adapter = MainAdapter()
    private val dbManager = ApparelDbManager(this);


    private val addApparelActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

                        adapter.addApparelInList(
                            Apparel(
                                id,
                                picture,
                                name,
                                company,
                                size,
                                description,
                                composition
                            )
                        )
                    }
                }
            }
        }

    private val modifyApparelActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

                        adapter.modifyApparelInList(
                            Apparel(
                                id,
                                picture,
                                name,
                                company,
                                size,
                                description,
                                composition
                            ), position
                        )
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

        val list = loadQueryAll();
        adapter.setApparelList(
            list
        )
        repositoryMock.setApparelListRepo(list);

        bindingMain.imageAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            addApparelActivityLauncher.launch(intent)
        }

    }

    override fun onDestroy() {
        dbManager.close()
        super.onDestroy()
    }


    fun loadQueryAll(): MutableList<Apparel> {
        val cursor = dbManager.queryAll()
        val apparelList = mutableListOf<Apparel>();
        if (cursor.moveToFirst()) {
            do {
                val idColumnIndex = cursor.getColumnIndex(ApparelContract.ApparelEntry.COLUMN_ID)
                val pictureIndex =
                    cursor.getColumnIndex(ApparelContract.ApparelEntry.COLUMN_PICTURE)
                val nameIndex = cursor.getColumnIndex(ApparelContract.ApparelEntry.COLUMN_NAME)
                val companyIndex =
                    cursor.getColumnIndex(ApparelContract.ApparelEntry.COLUMN_COMPANY)
                val sizeIndex = cursor.getColumnIndex(ApparelContract.ApparelEntry.COLUMN_SIZE)
                val descriptionIndex =
                    cursor.getColumnIndex(ApparelContract.ApparelEntry.COLUMN_DESCRIPTION)
                val compositionIndex =
                    cursor.getColumnIndex(ApparelContract.ApparelEntry.COLUMN_COMPOSITION)
                val id = cursor.getInt(idColumnIndex)
                val picture = cursor.getString(pictureIndex)
                val name = cursor.getString(nameIndex)
                val size = cursor.getString(sizeIndex)
                val company = cursor.getString(companyIndex)
                val description = cursor.getString(descriptionIndex)
                val composition = cursor.getString(compositionIndex)

                apparelList.add(
                    Apparel(
                        id,
                        picture,
                        name,
                        company,
                        Size.valueOf(size),
                        description,
                        composition
                    )
                )
            } while (cursor.moveToNext())
        }
        return apparelList;
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
            holder.binding.nameFromCard.text = "NAME: " + apparel.name
            holder.binding.sizeFromCard.text = "MARIME: " + apparel.size.toString()
            holder.binding.companyFromCard.text = "Company: " + apparel.company

            holder.binding.imageFromCard.load(apparel.picture)

            holder.binding.cardId.setOnClickListener {
                logd("pressed on item to modify")
                val intent = Intent(this@MainActivity, ModifyActivity::class.java)
                intent.putExtra(ModifyActivity.POSITION, position)
                intent.putExtra(ModifyActivity.PICTURE, apparel.picture)
                intent.putExtra(ModifyActivity.ID, apparel.id) //PUNEM IN BUNDLE
                intent.putExtra(ModifyActivity.NAME, apparel.name)
                intent.putExtra(ModifyActivity.COMPANY, apparel.company)
                intent.putExtra(ModifyActivity.SIZE, apparel.size.toString())
                intent.putExtra(ModifyActivity.DESCRIPTION, apparel.description)
                intent.putExtra(ModifyActivity.COMPOSITION, apparel.composition)
                modifyApparelActivityLauncher.launch(intent)
            }

            holder.binding.buttonDelete.setOnClickListener {
                val selectionArgs = arrayOf(apparels[position].id.toString())
                apparelRepository.remove(apparels[position].id)
                apparels.removeAt(position)
                dbManager.delete(
                    "${ApparelContract.ApparelEntry.COLUMN_ID}=?",
                    selectionArgs
                )
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
