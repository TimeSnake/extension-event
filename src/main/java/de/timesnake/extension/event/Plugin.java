package de.timesnake.extension.event;

public class Plugin extends de.timesnake.basic.bukkit.util.chat.Plugin {

    public static final Plugin EASTER = new Plugin("Easter", "XEE");
    public static final Plugin BIRTHDAY = new Plugin("Birthday", "XEB");
    public static final Plugin CHRISTMAS = new Plugin("Christmas", "XEC");

    protected Plugin(String name, String code) {
        super(name, code);
    }
}
