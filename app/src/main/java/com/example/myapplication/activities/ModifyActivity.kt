package com.example.myapplication.activities
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.myapplication.ApplicationUtils.ApplicationContainer
import com.example.myapplication.databinding.ModifyBinding
import com.example.myapplication.domain.Apparel
import com.example.myapplication.domain.Size
import com.example.myapplication.repository.Repository
import com.example.myapplication.toast

class ModifyActivity: AppCompatActivity(){


    private lateinit var bindingModify: ModifyBinding
    private val applicationContainer = ApplicationContainer()
    private val apparelRepo:Repository = applicationContainer.getSingletonApparelRepository()!!
    companion object{
        const val POSITION = "position"
        const val ID = "id"
        const val PICTURE = "picture"
        const val NAME = "name"
        const val COMPANY = "company"
        const val SIZE = "size"
        const val DESCRIPTION = "description"
        const val COMPOSITION = "composition"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingModify = ModifyBinding.inflate(layoutInflater)
        val view = bindingModify.root
        setContentView(view)

        val bundle: Bundle? = intent.extras
        var id:Int = -1
        var position = 0
        if (bundle != null) {
            bindingModify.imageModify.load(bundle.getString(PICTURE))
            bindingModify.companyEditText.setText(bundle.getString(COMPANY))
            bindingModify.nameEditText.setText(bundle.getString(NAME))
            bindingModify.sizeEditText.setText(bundle.getString(SIZE))
            bindingModify.descriptionEditText.setText(bundle.getString(DESCRIPTION))
            bindingModify.compositionEditText.setText(bundle.getString(COMPOSITION))
            id = bundle.getInt(ID)
            position = bundle.getInt(POSITION)
        }

        bindingModify.buttonModify.setOnClickListener{
            toast("Apparel has been modified successfully!")
            val picture = apparelRepo.getPicture(position)
            val company = bindingModify.companyEditText.text.toString()
            val name = bindingModify.nameEditText.text.toString()
            val size = bindingModify.sizeEditText.text.toString()
            val description = bindingModify.descriptionEditText.text.toString()
            val composition = bindingModify.compositionEditText.text.toString()

            val apparel = Apparel(id,picture,name,company, Size.valueOf(size),description,composition)

            apparelRepo.modify(apparel,position)

            val response = Intent()
            response.putExtra(ID,id)
            response.putExtra(POSITION,position)
            response.putExtra(NAME,name)
            response.putExtra(PICTURE,picture)
            response.putExtra(COMPANY,company)
            response.putExtra(SIZE,size)
            response.putExtra(DESCRIPTION,description)
            response.putExtra(COMPOSITION,composition)
            setResult(Activity.RESULT_OK,response)
            finish()

        }
    }
}