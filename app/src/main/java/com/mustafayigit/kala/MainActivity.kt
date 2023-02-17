package com.mustafayigit.kala

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mustafayigit.kala.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        with(ResourceManager) {
            outsourceStringProvider = ::getStringFromCache
            setLanguage("en")
            provideStrings(getEnglishStrings())

            with(mBinding) {
                bindResources()
                btnAction.setOnClickListener {
                    val lang = when (getCurrentLanguage()) {
                        "tr" -> "en"
                        "en" -> "tr"
                        else -> return@setOnClickListener
                    }
                    changeLanguage(lang)
                    bindResources()
                }
            }
        }
    }

    private fun bindResources() {
        with(mBinding) {
            with(ResourceManager) {
                txtLabel.text = ResourceKeys.HELLO()
                txtDescription.text = ResourceKeys.TEXT_FROM_OUTSOURCE()
                btnAction.text = ResourceKeys.CHANGE_LANGUAGE()
                imgIcon.setImageDrawable(ResourceKeys.COUNTRY_FLAG())
            }
        }
    }

    private fun changeLanguage(lang: String) {
        ResourceManager.clear()
        ResourceManager.setLanguage(lang)
        val strings = if (lang == "en") getEnglishStrings() else getTurkishStrings()
        ResourceManager.provideStrings(strings)
        with(ResourceManager) {
            showToast(ResourceKeys.SUCCESS())
        }
    }

    private fun getEnglishStrings(): Map<String, String> {
        return mapOf(
            "${ResourceKeys.HELLO}" to "Hello!",
            "${ResourceKeys.CHANGE_LANGUAGE}" to "Change Language",
            "${ResourceKeys.SUCCESS}" to "Success!",
        )
    }

    private fun getTurkishStrings(): Map<String, String> {
        return mapOf(
            "${ResourceKeys.HELLO}" to "Merhaba!",
            "${ResourceKeys.CHANGE_LANGUAGE}" to "Dil Değiştir",
            "${ResourceKeys.SUCCESS}" to "Başarılı!",
        )
    }

    private fun getStringFromCache(lang: String, key: String): String? {
        return when (lang) {
            "en" -> when (key) {
                "${ResourceKeys.TEXT_FROM_OUTSOURCE}" -> "This text is from cache."
                else -> null
            }
            "tr" -> when (key) {
                "${ResourceKeys.TEXT_FROM_OUTSOURCE}" -> "Bu metin cache'ten alındı."
                else -> null
            }
            else -> null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}