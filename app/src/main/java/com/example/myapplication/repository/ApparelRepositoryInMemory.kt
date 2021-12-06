package com.example.myapplication.repository

import com.example.myapplication.domain.Size
import com.example.myapplication.domain.Apparel
import java.security.AlgorithmParameterGenerator


class ApparelRepositoryInMemory : Repository {


    private var apparels:ArrayList<Apparel> = ArrayList(
    )

    override fun setApparelListRepo(apparelss: List<Apparel>) {
        apparels = apparelss as ArrayList<Apparel>
    }

    override fun findAll(): List<Apparel> {
        return apparels
    }

    override fun remove(id: Int) {
        var i = 0
        for( elem in apparels){
            if(elem.id == id){
                apparels.removeAt(i)
                break
            }
            i++
        }
    }

    override fun add(apparel: Apparel): Int {
        apparels.add(apparel)
        return apparels.size - 1
    }

    override fun modify(apparel: Apparel, id: Int) {
        var i = 0
        for( elem in apparels){
            if(elem.id == id){
                apparels[i] = apparel;
                break
            }
            i++
        }
    }

    override fun getPicture(id: Int): String {
        var pict = ""
        for( i in 0..apparels.size){
            if(apparels[i].id == id){
                pict =  apparels[i].picture
            }
        }
        return pict
    }
}