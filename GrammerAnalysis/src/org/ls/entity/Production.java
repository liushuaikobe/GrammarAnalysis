package org.ls.entity;

import java.util.ArrayList;

public class Production {
	// 产生式的左部
	private String Left;
	// 产生式的右部
	private ArrayList<String> right;
	// 产生式的Select集
	public ArrayList<String> Select;

	public Production(String left, ArrayList<String> right) {
		Left = left;
		this.right = right;
		this.Select = new ArrayList<String>();
	}

	public String getLeft() {
		return Left;
	}

	public void setLeft(String left) {
		Left = left;
	}

	public ArrayList<String> getRight() {
		return right;
	}

	public void setRight(ArrayList<String> right) {
		this.right = right;
	}

}
