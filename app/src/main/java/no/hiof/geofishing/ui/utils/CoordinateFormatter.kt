package no.hiof.geofishing.ui.utils

import java.text.DecimalFormat

object CoordinateFormatter {
    fun prettyLocation(latitude: Double?, longitude: Double?): String{
        val dec = DecimalFormat("#.##")
        var eastWest = "E"
        var northSouth = "N"
        var lat = latitude
        var long = longitude
        return if (lat != null && long != null) {
            if(lat < 0 ){
                lat *= -1
                northSouth = "S"
            }
            if(long < 0){
                long *=-1
                eastWest = "W"
            }
            dec.format(lat).toString() + northSouth + " " + dec.format(long).toString() + eastWest
        } else ""
    }
}