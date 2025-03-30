package top.eiyooooo.easycontrol.app.entity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import top.eiyooooo.easycontrol.app.BuildConfig
import top.eiyooooo.easycontrol.app.helper.get
import top.eiyooooo.easycontrol.app.helper.put

object Preferences {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var flowSharedPreferences: FlowSharedPreferences

    fun init(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        this.sharedPreferences = sharedPreferences
        this.flowSharedPreferences = FlowSharedPreferences(sharedPreferences)

        if (!sharedPreferences.contains("others.enable_log")) {
            enableLog = BuildConfig.DEBUG
        }
    }

    var darkTheme
        get() = sharedPreferences.get("appearance.dark_theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        set(value) = sharedPreferences.put("appearance.dark_theme", value)

    val darkThemeFlow
        get() = flowSharedPreferences.getInt("appearance.dark_theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM).asFlow()

    var systemColor
        get() = sharedPreferences.get("appearance.system_color", true)
        set(value) = sharedPreferences.put("appearance.system_color", value)

    var enableLog
        get() = sharedPreferences.get("others.enable_log", BuildConfig.DEBUG)
        set(value) = sharedPreferences.put("others.enable_log", value)

    // ========== 从 Setting 迁移的属性 ==========

    var defaultLocale
        get() = sharedPreferences.get("defaultLocale", "")
        set(value) = sharedPreferences.put("defaultLocale", value)

    var defaultIsAudio
        get() = sharedPreferences.get("defaultIsAudio", true)
        set(value) = sharedPreferences.put("defaultIsAudio", value)

    var defaultMaxSize
        get() = sharedPreferences.get("defaultMaxSize", 1600)
        set(value) = sharedPreferences.put("defaultMaxSize", value)

    var defaultMaxFps
        get() = sharedPreferences.get("defaultMaxFps", 60)
        set(value) = sharedPreferences.put("defaultMaxFps", value)

    var defaultMaxVideoBit
        get() = sharedPreferences.get("defaultMaxVideoBit", 4)
        set(value) = sharedPreferences.put("defaultMaxVideoBit", value)

    var defaultSetResolution
        get() = sharedPreferences.get("defaultSetResolution", false)
        set(value) = sharedPreferences.put("defaultSetResolution", value)

    var defaultUseH265
        get() = sharedPreferences.get("defaultUseH265", true)
        set(value) = sharedPreferences.put("defaultUseH265", value)

    var defaultUseOpus
        get() = sharedPreferences.get("defaultUseOpus", true)
        set(value) = sharedPreferences.put("defaultUseOpus", value)

    var defaultClipboardSync
        get() = sharedPreferences.get("defaultClipboardSync", false)
        set(value) = sharedPreferences.put("defaultClipboardSync", value)

    var defaultNightModeSync
        get() = sharedPreferences.get("defaultNightModeSync", false)
        set(value) = sharedPreferences.put("defaultNightModeSync", value)

    var defaultFull
        get() = sharedPreferences.get("defaultFull", false)
        set(value) = sharedPreferences.put("defaultFull", value)

    var autoBackOnStartDefault
        get() = sharedPreferences.get("autoBackOnStartDefault", false)
        set(value) = sharedPreferences.put("autoBackOnStartDefault", value)

    var turnOnScreenIfStart
        get() = sharedPreferences.get("TurnOnScreenIfStart", true)
        set(value) = sharedPreferences.put("TurnOnScreenIfStart", value)

    var turnOffScreenIfStart
        get() = sharedPreferences.get("TurnOffScreenIfStart", false)
        set(value) = sharedPreferences.put("TurnOffScreenIfStart", value)

    var turnOffScreenIfStop
        get() = sharedPreferences.get("TurnOffScreenIfStop", false)
        set(value) = sharedPreferences.put("TurnOffScreenIfStop", value)

    var turnOnScreenIfStop
        get() = sharedPreferences.get("TurnOnScreenIfStop", true)
        set(value) = sharedPreferences.put("TurnOnScreenIfStop", value)

    var keepAwake
        get() = sharedPreferences.get("keepAwake", true)
        set(value) = sharedPreferences.put("keepAwake", value)

    var defaultShowNavBar
        get() = sharedPreferences.get("defaultShowNavBar", true)
        set(value) = sharedPreferences.put("defaultShowNavBar", value)

    var defaultMiniOnOutside
        get() = sharedPreferences.get("defaultMiniOnOutside", false)
        set(value) = sharedPreferences.put("defaultMiniOnOutside", value)

    var miniRecoverOnTimeout
        get() = sharedPreferences.get("miniRecoverOnTimeout", false)
        set(value) = sharedPreferences.put("miniRecoverOnTimeout", value)

    var fullToMiniOnExit
        get() = sharedPreferences.get("fullToMiniOnExit", true)
        set(value) = sharedPreferences.put("fullToMiniOnExit", value)

    var fillFull
        get() = sharedPreferences.get("fillFull", false)
        set(value) = sharedPreferences.put("fillFull", value)

    var newMirrorMode
        get() = sharedPreferences.get("newMirrorMode", true)
        set(value) = sharedPreferences.put("newMirrorMode", value)

    var forceDesktopMode
        get() = sharedPreferences.get("ForceDesktopMode", false)
        set(value) = sharedPreferences.put("ForceDesktopMode", value)

    var tryStartDefaultInAppTransfer
        get() = sharedPreferences.get("tryStartDefaultInAppTransfer", false)
        set(value) = sharedPreferences.put("tryStartDefaultInAppTransfer", value)

    var showReconnect
        get() = sharedPreferences.get("showReconnect", true)
        set(value) = sharedPreferences.put("showReconnect", value)

    var showConnectUSB
        get() = sharedPreferences.get("showConnectUSB", true)
        set(value) = sharedPreferences.put("showConnectUSB", value)

    var countdownTime
        get() = sharedPreferences.get("countdownTime", "5")
        set(value) = sharedPreferences.put("countdownTime", value)

    var alwaysFullMode
        get() = sharedPreferences.get("alwaysFullMode", false)
        set(value) = sharedPreferences.put("alwaysFullMode", value)

    var showUsage
        get() = sharedPreferences.get("showUsage", false)
        set(value) = sharedPreferences.put("showUsage", value)

    var enableUSB
        get() = sharedPreferences.get("enableUSB", true)
        set(value) {
            sharedPreferences.put("enableUSB", value)
            if (value && this::sharedPreferences.isInitialized) AppData.myBroadcastReceiver.checkConnectedUsb(AppData.main)
        }

    var setFullScreen
        get() = sharedPreferences.get("setFullScreen", true)
        set(value) = sharedPreferences.put("setFullScreen", value)

    var audioChannel
        get() = sharedPreferences.get("audioChannel", 0)
        set(value) = sharedPreferences.put("audioChannel", value)

    var monitorState
        get() = sharedPreferences.get("monitorState", false)
        set(value) = sharedPreferences.put("monitorState", value)

    var monitorLatency
        get() = sharedPreferences.get("monitorLatency", 1500)
        set(value) = sharedPreferences.put("monitorLatency", value)
}
