package uk.ac.tees.mad.w9617329


data class DonarModel(    val name: String,
                         val phone : String,
                         val image_url: String,
                         val bloodType : String,
                         val email : String,
                          val lat : Double,
                            val long : Double,
                            val isdonar : Boolean) {
    constructor() : this("", "", "", "", "" ,0.0,0.0,false)
}