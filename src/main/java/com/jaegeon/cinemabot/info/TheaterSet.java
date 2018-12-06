package com.jaegeon.cinemabot.info;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class TheaterSet {
	private static HashMap<Integer,LinkedList<String>> theaterMap;
	private static final String[][] thIDList = {{"100", "CGV"}, {"200", "롯데시네마"}};

	static {
		theaterMap = new HashMap<Integer, LinkedList<String>>();
		theaterMap.put(100,
				new LinkedList<String>(Arrays.asList("CGV", "cgv","씨지브이","씨지비","시지비")));
		theaterMap.put(200,
				new LinkedList<String>(Arrays.asList("롯데시네마", "lotte","lottecinema","롯시")));
		theaterMap.put(300,
				new LinkedList<String>(Arrays.asList("메가박스", "megabox","mega","box","메박")));
	}

	public static HashMap<Integer, LinkedList<String>> getTheaterMap() {
		return theaterMap;
	}
	public static String[][] getThIDList() {
		return thIDList;
	}
}
