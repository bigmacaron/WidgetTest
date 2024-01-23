package kr.kro.fatcats.widgettest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Job


class MainActivity : AppCompatActivity() {

    companion object {
        const val SHARED_PRES = "prefs"
        const val IMAGE_INDEX_CAT = "imageIndexCat"
        const val IMAGE_INDEX_PEOPLE = "imageIndexPeople"
        var changeJob : Job? = null
        val imageListCat = intArrayOf(
            R.mipmap.im1,R.mipmap.im2,R.mipmap.im3,R.mipmap.im4,R.mipmap.im5,R.mipmap.im6,R.mipmap.im7,R.mipmap.im8,R.mipmap.im9,R.mipmap.im10,R.mipmap.im11,
            R.mipmap.im12,R.mipmap.im13,R.mipmap.im14,R.mipmap.im15,R.mipmap.im16,R.mipmap.im17,R.mipmap.im18,R.mipmap.im19,R.mipmap.im20,R.mipmap.im21,
            R.mipmap.im22,R.mipmap.im23,R.mipmap.im24,R.mipmap.im25,R.mipmap.im26,R.mipmap.im27,R.mipmap.im28,R.mipmap.im29,R.mipmap.im30)
        val imageListPeople = intArrayOf(
            R.drawable.ic_emoji_people ,R.drawable.ic_directions_walk , R.drawable.ic_directions_run , R.drawable.ic_directions_bike)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences(SHARED_PRES, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(IMAGE_INDEX_CAT, 0)
        editor.putInt(IMAGE_INDEX_PEOPLE, 0)
        editor.apply()
    }
}

