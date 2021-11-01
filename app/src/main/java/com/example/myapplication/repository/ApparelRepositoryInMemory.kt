package com.example.myapplication.repository

import com.example.myapplication.domain.Size
import com.example.myapplication.domain.Apparel
import java.security.AlgorithmParameterGenerator


class ApparelRepositoryInMemory : Repository {


    private var apparels:ArrayList<Apparel> = ArrayList(
        listOf(
            Apparel(1,"https://cdn-icons-png.flaticon.com/512/2290/2290486.png","adidasi","adidas",
                Size.S_40,"foarte faini","piele"),
            Apparel(2,"https://image.flaticon.com/icons/png/512/2872/2872508.png","adidasi","nike",
                 Size.S_39,"foarte buni","material"),
            Apparel(2,"https://cdn3.iconfinder.com/data/icons/casual-wear-1/100/jacket-512.png","jacket","Tommy", Size.L,"interesanti","material"),
            Apparel(2,"https://icon-library.com/images/jeans-icon/jeans-icon-10.jpg","jeans","Pollo", Size.S_40,"foarte buni","bumbac"),
            )
    )

    override fun findAll(): List<Apparel> {
        return apparels
    }

    override fun remove(position: Int) {
        apparels.removeAt(position)
    }

    override fun add(apparel: Apparel): Int {
        apparels.add(apparel)
        return apparels.size - 1
    }

    override fun modify(apparel: Apparel, position: Int) {
        apparels[position] = apparel
    }

    override fun getPicture(position: Int): String {
        return apparels[position].picture
    }
}