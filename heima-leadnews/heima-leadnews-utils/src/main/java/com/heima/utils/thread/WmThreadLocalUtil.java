package com.heima.utils.thread;

import com.heima.model.wemedia.pojos.WmUser;

public class WmThreadLocalUtil {
    private final static ThreadLocal<WmUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();
//    deposit threads
    public static void setUser(WmUser wmUser) {
        WM_USER_THREAD_LOCAL.set(wmUser);
    }
//    get it from the thread
    public static WmUser getUser() {
        return WM_USER_THREAD_LOCAL.get();
    }
//    clean
    public static void clear() {
        WM_USER_THREAD_LOCAL.remove();
    }
}
