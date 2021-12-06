package com.example.myapplication.repository

import com.example.myapplication.domain.Apparel
import java.security.AlgorithmParameterGenerator

interface Repository {

    fun findAll():List<Apparel>

    fun remove(position:Int)

    fun add(apparel: Apparel):Int

    fun modify(apparel: Apparel,position:Int)

    fun getPicture(id: Int): String

    fun setApparelListRepo(apparelss: List<Apparel>)
}