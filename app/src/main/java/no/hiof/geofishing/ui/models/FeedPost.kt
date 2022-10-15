package no.hiof.geofishing.ui.models

import no.hiof.geofishing.R

data class FeedPost(
    val uid: Int,
    var title: String,
    var description: String,
    var posterUrl: Int
) {

    companion object {
        fun getFeedPosts(): List<FeedPost> {
            return listOf(
                FeedPost(0, "Test0", "Stor Fisk", R.drawable.fish_9),
                FeedPost(
                    1,
                    "Test1",
                    "Kjempe lang setning for å teste constraints på fragmentet så det " +
                            "går nok kjempebra 10/10 simenerbesteteststststsst mange ord jajajaj " +
                            "iajwif jaiwejf iajew fijaeif jaeij fiaje fijaei jfaief jiaej fiaejf " +
                            "iaej ifjaei jfiaejf iaejf iaej ifjaei fjaiej fiaj eijfa iej BLI " +
                            "DESSA" + " ORDA MED DA TRUUUUUUUU?`??????? END OF LINE",
                    R.drawable.fish_9
                ),
                FeedPost(2, "Test2", "Stor Fisk", R.drawable.fish_9),
                FeedPost(3, "Test3", "Stor Fisk", R.drawable.fish_9),
                FeedPost(4, "Test4", "Stor Fisk", R.drawable.fish_9),
                FeedPost(5, "Test5", "Stor Fisk", R.drawable.fish_9),
                FeedPost(6, "Test6", "Stor Fisk", R.drawable.fish_9),
                FeedPost(7, "Test7", "Stor Fisk", R.drawable.fish_9),
                FeedPost(8, "Test8", "Stor Fisk", R.drawable.fish_9),
                FeedPost(9, "Test9", "Stor Fisk", R.drawable.fish_9),
                FeedPost(10, "Test10", "Stor Fisk", R.drawable.fish_9),
                FeedPost(11, "Test11", "Stor Fisk", R.drawable.fish_9),
                FeedPost(12, "Test12", "Stor Fisk", R.drawable.fish_9),
                FeedPost(13, "Test13", "Stor Fisk", R.drawable.fish_9),
                FeedPost(14, "Test14", "Stor Fisk", R.drawable.fish_9),
                FeedPost(15, "Test15", "Stor Fisk", R.drawable.fish_9),
                FeedPost(16, "Test16", "Stor Fisk", R.drawable.fish_9),
                FeedPost(17, "Test17", "Stor Fisk", R.drawable.fish_9),
                FeedPost(18, "Test18", "Stor Fisk", R.drawable.fish_9),
                FeedPost(19, "Test19", "Stor Fisk", R.drawable.fish_9),
                FeedPost(20, "Test20", "Stor Fisk", R.drawable.fish_9),
                FeedPost(21, "Test21", "Stor Fisk", R.drawable.fish_9),
                FeedPost(22, "Test22", "Stor Fisk", R.drawable.fish_9),
                FeedPost(23, "Test23", "Stor Fisk", R.drawable.fish_9),
                FeedPost(24, "Test24", "Stor Fisk", R.drawable.fish_9),
                FeedPost(25, "Test25", "Stor Fisk", R.drawable.fish_9),
                FeedPost(26, "Test26", "Stor Fisk", R.drawable.fish_9),
                FeedPost(27, "Test27", "Stor Fisk", R.drawable.fish_9),
                FeedPost(28, "Test28", "Stor Fisk", R.drawable.fish_9),
                FeedPost(29, "Test29", "Stor Fisk", R.drawable.fish_9),
                FeedPost(30, "Test30", "Stor Fisk", R.drawable.fish_9),
                FeedPost(31, "Test31", "Stor Fisk", R.drawable.fish_9),
                FeedPost(32, "Test32", "Stor Fisk", R.drawable.fish_9),
                FeedPost(33, "Test33", "Stor Fisk", R.drawable.fish_9),
                FeedPost(34, "Test34", "Stor Fisk", R.drawable.fish_9),
                FeedPost(34, "Test34", "Stor Fisk", R.drawable.fish_9),
                FeedPost(35, "Test35", "Stor Fisk", R.drawable.fish_9),
            )
        }
    }
}