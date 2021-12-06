package com.example.myapplication.activities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ApplicationUtils.ApplicationContainer
import com.example.myapplication.databinding.AddBinding
import com.example.myapplication.domain.Apparel
import com.example.myapplication.domain.Size
import com.example.myapplication.repository.Repository
import com.example.myapplication.repository.db.ApparelContract
import com.example.myapplication.repository.db.ApparelDbManager
import com.example.myapplication.toast


class AddActivity:AppCompatActivity(){

    private lateinit var bindingAdd: AddBinding
    private val applicationContainer = ApplicationContainer()
    private val apparelRepo: Repository = applicationContainer.getSingletonApparelRepository()!!
    private val dbManager = ApparelDbManager(this);

    companion object{
        const val ID = "id"
        const val PICTURE = "picture"
        const val NAME = "name"
        const val COMPANY = "company"
        const val SIZE = "size"
        const val DESCRIPTION = "description"
        const val COMPOSITION = "composition"
    }

    private val pickImage = 100
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingAdd = AddBinding.inflate(layoutInflater)
        val view = bindingAdd.root
        setContentView(view)

        bindingAdd.buttonAdd.setOnClickListener {
            toast("Apparel has been added successfully!")

            val picture = bindingAdd.urlEditText.text.toString()
            val company = bindingAdd.companyEditText.text.toString()
            val name = bindingAdd.nameEditText.text.toString()
            val size = bindingAdd.sizeEditText.text.toString()
            val description = bindingAdd.descriptionEditText.text.toString()
            val composition = bindingAdd.compositionEditText.text.toString()

            val apparel =
                Apparel(picture, name, company, Size.valueOf(size), description, composition)

            val values = ContentValues().apply {
                put(ApparelContract.ApparelEntry.COLUMN_PICTURE, picture)
                put(ApparelContract.ApparelEntry.COLUMN_COMPANY, company)
                put(ApparelContract.ApparelEntry.COLUMN_NAME, name)
                put(ApparelContract.ApparelEntry.COLUMN_SIZE, size)
                put(ApparelContract.ApparelEntry.COLUMN_DESCRIPTION, description)
                put(ApparelContract.ApparelEntry.COLUMN_COMPOSITION, composition)
            }
            val id = dbManager.insert(values).toInt()
            apparelRepo.add(apparel)

            val response = Intent()
            response.putExtra(ID, id)
            response.putExtra(NAME, name)
            response.putExtra(PICTURE, picture)
            response.putExtra(COMPANY, company)
            response.putExtra(SIZE, size)
            response.putExtra(DESCRIPTION, description)
            response.putExtra(COMPOSITION, composition)
            setResult(Activity.RESULT_OK, response)
            finish()

        }
    }
}