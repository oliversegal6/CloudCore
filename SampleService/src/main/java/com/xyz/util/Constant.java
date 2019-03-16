package com.xyz.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.xyz.pojo.Concept;
import com.xyz.pojo.Stock;

public class Constant {

	public static List<Concept> CONCEPT_CACHE = new ArrayList<Concept>();
	public static Map<String, String> CONCEPT_MAP_CACHE = new HashMap<String, String>();
	public static Map<String, JSONObject>  FINA_INDICATOR_CACHE = new HashMap<String, JSONObject> ();
	public static Map<String, List<JSONObject>>   TOP10_HOLDERS_CACHE = new HashMap<String, List<JSONObject>> ();
	public static List<JSONObject>  HIST_STOCK_CACHE = new ArrayList<JSONObject> ();
	public static List<Stock> STOCK_CACHE = new ArrayList<Stock>();
}
