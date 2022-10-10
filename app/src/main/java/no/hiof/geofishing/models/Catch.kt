package no.hiof.geofishing.models

data class Catch(val uid: Int, var title: String, var description: String, var species: String, var weight: Int, var length: Int, var pictureId: Int?, var fishingRod: String?, var fishingLure: String?) {


    companion object {
        fun getCatch() : Catch {
            return Catch(1,
                "Torsk i glomma!",
                "Kan ikke så mye om fisk, men skal det være torsk i Glomma?",
                "Cod",
                1200,
                30,
                null,
                "Shimano Sienna Spinning Combo SIENNA 4000",
                "Berkley Rattling Powerblade"
                )
        }
    }

}