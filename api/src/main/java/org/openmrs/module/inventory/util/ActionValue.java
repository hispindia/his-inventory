package org.openmrs.module.inventory.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class ActionValue {

	

	public static final int[] DRUG_ATTRIBUTE = { 1, 2,3 };
	public static final String[] DRUG_ATTRIBUTE_NAMES = {"Vital", "Essential","Desired" };
	
	public static final int[] IS_DRUG = { 1, 2 };
	public static final String[] IS_DRUG_NAMES = {"Yes", "No" };
	
	public static final int[] ITEM_ATTRIBUTE = { 1, 2 };
	public static final String[] ITEM_ATTRIBUTE_NAMES = {"Essential Item", "Non Essential Item" };
	
	
	public static final int[] INDENT_SUBSTORE = { 1, 2,3,4,5,6};
	public static final String[] INDENT_SUBSTORE_NAMES = {"SAVE", "SENT","RECEIPT","REFUSE","DONE","MAIN-STORE REFUSE" };
	
	public static final int[] TRANSACTION = { 1, 2};
	public static final List<String> TRANSACTION_NAMES = Collections.unmodifiableList(Arrays.asList("RECEIPT", "ISSUE"));
	
	public static String getIndentSubStoreName(int pos) {
		if(ArrayUtils.contains(INDENT_SUBSTORE, pos)) {
			return INDENT_SUBSTORE_NAMES[ArrayUtils.indexOf(INDENT_SUBSTORE, pos)];
		}
		
		return " ";
	}
	
	public static List<Action> getListIndentSubStore() {
		List<Action> rs = new ArrayList<Action>();
		for (int i = 0; i < INDENT_SUBSTORE_NAMES.length; i++) {
			rs.add(new Action(INDENT_SUBSTORE[i], INDENT_SUBSTORE_NAMES[i]));
		}
		return rs;
	}
	public static String getIsDrugName(int pos) {
		if(ArrayUtils.contains(IS_DRUG, pos)) {
			return IS_DRUG_NAMES[ArrayUtils.indexOf(IS_DRUG, pos)];
		}
		
		return " ";
	}
	
	public static List<Action> getListIsDrug() {
		List<Action> rs = new ArrayList<Action>();
		for (int i = 0; i < IS_DRUG_NAMES.length; i++) {
			rs.add(new Action(IS_DRUG[i], IS_DRUG_NAMES[i]));
		}
		return rs;
	}
	
	public static final int[] INDENT_MAINSTORE = { 1, 2,3,4};
	public static final String[] INDENT_MAINSTORE_NAMES = {"WAIT PROCESS","REFUSE","DONE","SUB-STORE REFUSE" };
	
	
	public static String getIndentMainbStoreName(int pos) {
		if(ArrayUtils.contains(INDENT_MAINSTORE, pos)) {
			return INDENT_MAINSTORE_NAMES[ArrayUtils.indexOf(INDENT_MAINSTORE, pos)];
		}
		
		return " ";
	}
	
	public static List<Action> getListIndentMainStore() {
		List<Action> rs = new ArrayList<Action>();
		for (int i = 0; i < INDENT_MAINSTORE_NAMES.length; i++) {
			rs.add(new Action(INDENT_MAINSTORE[i], INDENT_MAINSTORE_NAMES[i]));
		}
		return rs;
	}

	

	

	
	public static String getDrugAttribute(int pos) {
		if(ArrayUtils.contains(DRUG_ATTRIBUTE, pos)) {
			return DRUG_ATTRIBUTE_NAMES[ArrayUtils.indexOf(DRUG_ATTRIBUTE, pos)];
		}
		
		return " ";
	}
	
	public static String getItemAttribute(int pos) {
		if(ArrayUtils.contains(ITEM_ATTRIBUTE, pos)) {
			return ITEM_ATTRIBUTE_NAMES[ArrayUtils.indexOf(ITEM_ATTRIBUTE, pos)];
		}
		
		return " ";
	}
	public static List<Action> getListItemAttribute() {
		List<Action> rs = new ArrayList<Action>();
		for (int i = 0; i < ITEM_ATTRIBUTE_NAMES.length; i++) {
			rs.add(new Action(ITEM_ATTRIBUTE[i], ITEM_ATTRIBUTE_NAMES[i]));
		}
		return rs;
	}
	public static List<Action> getListDrugAttribute() {
		List<Action> rs = new ArrayList<Action>();
		for (int i = 0; i < DRUG_ATTRIBUTE_NAMES.length; i++) {
			rs.add(new Action(DRUG_ATTRIBUTE[i], DRUG_ATTRIBUTE_NAMES[i]));
		}
		return rs;
	}
	
	
	public static String getTransactionName(int pos) {
		if(ArrayUtils.contains(TRANSACTION, pos)) {
			return ActionValue.TRANSACTION_NAMES.get(ArrayUtils.indexOf(TRANSACTION, pos));
		}
		
		return " ";
	}
	
	public static List<Action> getListTypeTransaction() {
		List<Action> rs = new ArrayList<Action>();
		for (int i = 0; i < TRANSACTION_NAMES.size(); i++) {
			rs.add(new Action(TRANSACTION[i], TRANSACTION_NAMES.get(i)));
		}
		return rs;
	}
	


	public static void main(String[] args) {
		int[] TYPE = { 1, 2,3 };
		System.out.println(TYPE[1-1]);
	}

}
