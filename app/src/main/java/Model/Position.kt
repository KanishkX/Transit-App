package Model

class Position (var lon:Int,var lat:Int){
    fun Position(longitude: Int,latitude: Int){
        this.lat = latitude
        this.lon = longitude
    }

    fun getLatitude():Int{
        return this.lat
    }
    fun getLongitude():Int{
        return this.lon
    }

}