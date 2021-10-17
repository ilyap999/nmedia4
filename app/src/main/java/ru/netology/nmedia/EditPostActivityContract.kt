package ru.netology.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class EditPostActivityContract: ActivityResultContract<String, String?>() {
    override fun createIntent(context: Context, input: String?): Intent =
        Intent(context, EditPostActivity::class.java).putExtra(Intent.EXTRA_TEXT, input)

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
}
/*class NewPostActivityContract: ActivityResultContract<String, String?>() {
    override fun createIntent(context: Context, input: Unit?): Intent =
        Intent(context, NewPostActivity::class.java).putExtra()*/

/*
class NewPostActivityContract: ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit?): Intent =
        Intent(context, NewPostActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
}*/
