package com.example.myapplication.domain

data class Apparel(
    var id: Int,
    var picture: String,
    var name: String,
    var company:String,
    var size: Size,
    var description:String,
    var composition:String

)
{
    constructor(picture: String,name: String,company: String,size: Size,description: String,composition: String):
            this(0,picture,name,company,size,description,composition)
}
