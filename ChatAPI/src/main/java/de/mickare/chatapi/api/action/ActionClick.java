package de.mickare.chatapi.api.action;

import java.util.Map;

import com.google.common.collect.Maps;

public enum ActionClick {

	OPEN_URL("OPEN_URL", 0, "open_url", true), OPEN_FILE("OPEN_FILE", 1,
			"open_file", false), RUN_COMMAND("RUN_COMMAND", 2, "run_command",
			true), TWITCH_USER_INFO("TWITCH_USER_INFO", 3, "twitch_user_info",
			false), SUGGEST_COMMAND("SUGGEST_COMMAND", 4, "suggest_command",
			true);
	private static final Map<String, ActionClick> f = Maps.newHashMap();
	private final boolean flag;
	private final String name;

	// private static final EnumClickAction[] i = new EnumClickAction[] {
	// OPEN_URL, OPEN_FILE, RUN_COMMAND, TWITCH_USER_INFO, SUGGEST_COMMAND};

	private ActionClick(String toString, int i, String name, boolean flag) {
		this.name = name;
		this.flag = flag;
	}

	public boolean isFlag() {
		return this.flag;
	}

	public String getName() {
		return this.name;
	}

	public static ActionClick fromString(String s) {
		return (ActionClick) f.get(s);
	}

	static {
		ActionClick[] aenumclickaction = values();
		int i = aenumclickaction.length;

		for (int j = 0; j < i; ++j) {
			ActionClick enumclickaction = aenumclickaction[j];

			f.put(enumclickaction.getName(), enumclickaction);
		}
	}
}
