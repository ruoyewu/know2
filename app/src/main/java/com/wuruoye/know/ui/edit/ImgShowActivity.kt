package com.wuruoye.know.ui.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wuruoye.know.R
import com.wuruoye.know.util.model.beans.ImagePath

/**
 * Created at 2019-04-28 10:42 by wuruoye
 * Description:
 */
class ImgShowActivity : AppCompatActivity() {
    private lateinit var iv: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_img_show)

        iv = findViewById(R.id.iv_img_show)

        val imgPath: ImagePath = intent.getParcelableExtra(IMG_PATH)
        Glide.with(this)
            .load(imgPath.localPath)
            .error(Glide.with(this)
                .load(imgPath.remotePath))
            .into(iv)
    }

    companion object {
        const val IMG_PATH = "img_path"

        fun show(context: Context, imgPath: ImagePath) {
            val intent = Intent(context, ImgShowActivity::class.java)
            intent.putExtra(IMG_PATH, imgPath)
            context.startActivity(intent)
        }
    }
}