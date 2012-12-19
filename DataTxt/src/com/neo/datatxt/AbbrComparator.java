package com.neo.datatxt;

import java.util.Comparator;
import java.util.HashMap;

public class AbbrComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		String str1 = ((HashMap<String, String>) arg0).get("abbr");
		String str2 = ((HashMap<String, String>) arg1).get("abbr");
		return str1.compareTo(str2);
	}

}
