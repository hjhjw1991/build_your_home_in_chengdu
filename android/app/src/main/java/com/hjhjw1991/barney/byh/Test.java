package com.hjhjw1991.barney.byh;


import android.util.Log;

import com.hjhjw1991.barney.byh.service.impl.FragmentTest;

/**
 * @author huangjun.barney
 * @since 2021/12/24
 */
class Test {
    public static void test() {
        try {
            new FragmentTest("", null);
        }catch(NullPointerException e) {
            Log.e("hjhj", Log.getStackTraceString(e));
        }
    }
}
