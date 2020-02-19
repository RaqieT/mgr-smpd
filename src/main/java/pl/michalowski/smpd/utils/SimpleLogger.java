package pl.michalowski.smpd.utils;

public class SimpleLogger {
    private String className;

    public SimpleLogger(Class clazz) {
        this.className = clazz.getSimpleName();
    }

    public void log(String info) {
        System.out.println(className + ": " + info);
    }
}
