package de.mickare.chatapi.api.action;

import java.util.HashMap;
import java.util.Map;

public enum ActionHover {

    SHOW_TEXT("SHOW_TEXT", 0, "show_text", true), SHOW_ACHIEVEMENT("SHOW_ACHIEVEMENT", 1, "show_achievement", true), SHOW_ITEM("SHOW_ITEM", 2, "show_item", true);
    private static final Map<String, ActionHover> d = new HashMap<>();
    private final boolean flag;
    private final String name;
    //private static final EnumHoverAction[] g = new EnumHoverAction[] { SHOW_TEXT, SHOW_ACHIEVEMENT, SHOW_ITEM};

    private ActionHover(String s, int i, String s1, boolean flag) {
        this.name = s1;
        this.flag = flag;
    }

    public boolean isFlag() {
        return this.flag;
    }

    public String getName() {
        return this.name;
    }

    public static ActionHover fromString(String s) {
        return (ActionHover) d.get(s);
    }

    static {
        ActionHover[] aenumhoveraction = values();
        int i = aenumhoveraction.length;

        for (int j = 0; j < i; ++j) {
            ActionHover enumhoveraction = aenumhoveraction[j];

            d.put(enumhoveraction.getName(), enumhoveraction);
        }
    }
}