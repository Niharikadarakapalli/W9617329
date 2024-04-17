package uk.ac.tees.mad.w9617329


data class DonarModel(    val name: String,
                         val phone : String,
                         val image_url: String,
                         val bloodType : String,
                         val email : String) {
    constructor() : this("", "", "", "", "" )
}