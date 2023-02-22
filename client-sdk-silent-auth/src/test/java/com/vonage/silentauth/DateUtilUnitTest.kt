package com.vonage.silentauth

import android.os.Build
import com.vonage.silentauth.network.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class DateUtilUnitTest {
    @Test
    fun date_iscorrect() {
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 23)
        val version_A = Build.VERSION.SDK_INT
        assertEquals(version_A, 23)
        val date_A = DateUtils.now()
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 26)
        val version_B = Build.VERSION.SDK_INT
        assertEquals(version_B, 26)

        val date_B = DateUtils.now()
        assertEquals(date_A.substring(0, 18), date_B.substring(0, 18))
    }
}
