package com.rarnu.tools.neo.fragment

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.view.Menu
import android.widget.Toast
import com.rarnu.tools.neo.R
import com.rarnu.tools.neo.api.DeviceAPI
import com.rarnu.tools.neo.base.BasePreferenceFragment
import com.rarnu.tools.neo.comp.PreferenceEx
import com.rarnu.tools.neo.utils.AppUtils
import com.rarnu.tools.neo.xposed.XpStatus

/**
 * Created by rarnu on 11/23/16.
 */
class SettingsFragment : BasePreferenceFragment(), Preference.OnPreferenceClickListener {

    private var pMode: PreferenceEx? = null
    private var pAdChoose: PreferenceEx? = null
    private var pDeepClean: PreferenceEx? = null
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    override fun getBarTitle(): Int = R.string.settings_name

    override fun getCustomTitle(): String? = null

    override fun initComponents() {
        pref = context?.getSharedPreferences(XpStatus.PREF, if (Build.VERSION.SDK_INT < 24) 1 else 0)
        editor = pref?.edit()
        pMode = findPref(R.string.id_settings_mode)
        pAdChoose = findPref(R.string.id_settings_adchoose)
        pDeepClean = findPref(R.string.id_settings_deep_clean)
    }

    private fun findPref(prefId: Int): PreferenceEx = findPreference(getString(prefId)) as PreferenceEx

    override fun initEvents() {
        pMode?.onPreferenceClickListener = this
        pAdChoose?.onPreferenceClickListener = this
        pDeepClean?.onPreferenceClickListener = this
    }

    override fun initLogic() {
        pMode?.status = pref!!.getBoolean(XpStatus.KEY_WORK_MODE, false)
        pMode?.setSummary(if (pref!!.getBoolean(XpStatus.KEY_WORK_MODE, false)) R.string.settings_mode_effect else R.string.settings_mode_common)
        pAdChoose?.status = pref!!.getBoolean(XpStatus.KEY_AD_CHOOSE, false)
        pAdChoose?.setSummary(if (pref!!.getBoolean(XpStatus.KEY_AD_CHOOSE, false)) R.string.settings_adchoose_detail else R.string.settings_adchoose_onekey)
        pDeepClean?.status = pref!!.getBoolean(XpStatus.KEY_DEEP_CLEAN, false)

        val isMIUI = AppUtils.isMIUI(context)
        if (!isMIUI) {
            preferenceScreen.removePreference(pAdChoose)
        }
    }

    override fun getFragmentLayoutResId(): Int = R.xml.settings

    override fun getMainActivityName(): String? = null

    override fun initMenu(menu: Menu?) {

    }

    override fun onGetNewArguments(bn: Bundle?) {
    }

    override fun getFragmentState(): Bundle? = null

    override fun onPreferenceClick(preference: Preference): Boolean {
        val prefKey = preference.key
        val ex = preference as PreferenceEx
        if (prefKey == getString(R.string.id_settings_mode)) {
            ex.status = !ex.status
            editor?.putBoolean(XpStatus.KEY_WORK_MODE, ex.status)?.apply()
            DeviceAPI.makePreferenceReadable(Build.VERSION.SDK_INT, context?.packageName)
            pMode?.setSummary(if (pref!!.getBoolean(XpStatus.KEY_WORK_MODE, false)) R.string.settings_mode_effect else R.string.settings_mode_common)
            Toast.makeText(context, R.string.toast_reboot_app, Toast.LENGTH_LONG).show()
        } else if (prefKey == getString(R.string.id_settings_adchoose)) {
            ex.status = !ex.status
            editor
                    ?.putBoolean(XpStatus.KEY_AD_CHOOSE, ex.status)
                    ?.putBoolean(XpStatus.KEY_REMOVEAD, false)
                    ?.putBoolean(XpStatus.KEY_AD_BROWSER, false)
                    ?.putBoolean(XpStatus.KEY_AD_CALENDAR, false)
                    ?.putBoolean(XpStatus.KEY_AD_CLEANMASTER, false)
                    ?.putBoolean(XpStatus.KEY_AD_DOWNLOAD, false)
                    ?.putBoolean(XpStatus.KEY_AD_FILEEXPLORER, false)
                    ?.putBoolean(XpStatus.KEY_AD_CONTACTS, false)
                    ?.putBoolean(XpStatus.KEY_AD_MMS, false)
                    ?.putBoolean(XpStatus.KEY_AD_SEARCHBOX, false)
                    ?.putBoolean(XpStatus.KEY_AD_VIDEO, false)
                    ?.putBoolean(XpStatus.KEY_AD_MUSIC, false)
                    ?.putBoolean(XpStatus.KEY_AD_WEATHER, false)
                    ?.putBoolean(XpStatus.KEY_AD_THEMEMANAGER, false)
                    ?.putBoolean(XpStatus.KEY_AD_MARKET, false)
                    ?.putBoolean(XpStatus.KEY_AD_SETTINGS, false)
                    ?.putBoolean(XpStatus.KEY_AD_SYSTEM, false)
                    ?.apply()
            DeviceAPI.makePreferenceReadable(Build.VERSION.SDK_INT, context?.packageName)
            pAdChoose?.setSummary(if (pref!!.getBoolean(XpStatus.KEY_AD_CHOOSE, false)) R.string.settings_adchoose_detail else R.string.settings_adchoose_onekey)
        } else if (prefKey == getString(R.string.id_settings_deep_clean)) {
            ex.status = !ex.status
            editor?.putBoolean(XpStatus.KEY_DEEP_CLEAN, ex.status)?.apply()
            DeviceAPI.makePreferenceReadable(Build.VERSION.SDK_INT, context?.packageName)
        }
        return true
    }
}
