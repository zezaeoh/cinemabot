package com.jaegeon.cinemabot.info;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CommandSet {
	private static HashMap<String, LinkedList<String>> commandMap;

	static {
		commandMap = new LinkedHashMap<String, LinkedList<String>>();
		commandMap.put("박스오피스",
				new LinkedList<String>(
						Arrays.asList("박스오피스", "영화순위", "재밌는영화", "재밌는영화들")));
		commandMap.put("상영시간표",
				new LinkedList<String>(
						Arrays.asList("상영시간표", "시간표", "영화상영시간표", "영화시간표", "영화", "영화들")));
	}

	public static HashMap<String, LinkedList<String>> getCommandMap() {
		return commandMap;
	}
	
}

