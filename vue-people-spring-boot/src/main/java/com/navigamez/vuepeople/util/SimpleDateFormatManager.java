package com.navigamez.vuepeople.util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleDateFormatManager {

    private static final Map<String, ThreadLocal<SimpleDateFormat>> localsByFormat = Collections.synchronizedMap(new HashMap<>());

    public static SimpleDateFormat get(String format) {
        return local(format).get();
    }

    private static ThreadLocal<SimpleDateFormat> local(String format) {
        if (format == null) {
            return null;
        }
        ThreadLocal<SimpleDateFormat> local = localsByFormat.get(format);
        if (local == null) {
            synchronized (localsByFormat) {
                local = localsByFormat.get(format);
                if (local == null) {
                    local = ThreadLocal.withInitial(() -> new SimpleDateFormat(format));
                    localsByFormat.put(format, local);
                }
            }
        }
        return local;
    }
}
