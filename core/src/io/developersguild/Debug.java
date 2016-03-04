package io.developersguild;

import com.badlogic.gdx.Gdx;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public abstract class Debug {
    // classes whose fields not to inspect with details()
    private static List<Class> baseClasses = Arrays.asList(new Class[]{
            Object.class,
            //TODO mute fields with this
    });

    public static void print(String s) {
        Gdx.app.log("", s);
    }

    public static void dbt(Object o) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        dbg_delegate(Arrays.toString(trace) + " >>> " + o);
    }

    public static void dbg_delegate(Object o) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int level = 3;
        print(trace[level].getClassName().replaceAll("[a-zA-Z]*\\.", "") + "@" + trace[level].getLineNumber() + " " + o);
    }

    public static void dbg(Object o) {
        dbg_delegate(o);
    }

    public static void exception(Throwable t) {
        dbg_delegate(t.toString());
    }

    @Deprecated
    public static String debugToString(Object o) {
        String ret = "";
        if (o == null) {
            ret += ("NULL");
        } else {
            ret += (o.getClass());
            for (Field f : o.getClass().getFields()) {
                try {
                    ret += (f.getName() + ": " + f.get(o));
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }
            }
        }
        return ret;
    }

    public static void details(Object o) {
        Debug.dbg_delegate("Detailed class dump:");
        if (o != null) {
            Class c = o.getClass();
            print(c + "Dump of:" + System.identityHashCode(o));
            while (!baseClasses.contains(c)) {
                for (Field f : c.getDeclaredFields()) {
                    if ((f.getModifiers() & Modifier.STATIC) == 0) //does not have static bit set - do not print class fields
                        try {
                            f.setAccessible(true);
                            Object obj = f.get(o);
                            if (obj != null && obj.getClass().isArray()) {
                                for (int i = 0; i < Array.getLength(obj); i++) {
                                    print(" " + f.getName() + " " + i + " " + Array.get(obj, i));
                                }
                            } else {
                                print(" " + f.getName() + " " + obj);
                            }
                        } catch (Exception e) {
                            print("" + e);
                        }
                }
                c = c.getSuperclass();
            }
        }
    }

    public static void mark() {
        dbg_delegate("executed");
    }
}