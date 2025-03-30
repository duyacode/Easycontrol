package top.eiyooooo.easycontrol.app

import android.app.Application
import android.content.Context
import android.os.Build
import org.lsposed.hiddenapibypass.HiddenApiBypass
import timber.log.Timber
import top.eiyooooo.easycontrol.app.entity.Preferences
import top.eiyooooo.easycontrol.app.helper.FLog
import java.util.Date

lateinit var application: MyApplication private set

class MyApplication : Application() {

    companion object {
        lateinit var appStartTime: Date
            private set
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        appStartTime = Date()

        Preferences.init(this)

        FLog.init(this)
        if (Preferences.enableLog) FLog.startFLog()
        Timber.i("App started at: $appStartTime")
    }
}
