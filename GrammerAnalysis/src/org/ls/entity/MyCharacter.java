package org.ls.entity;

import java.util.ArrayList;
import java.util.List;


public abstract class MyCharacter {
	public String what;
	public List<String> First;
	public MyCharacter() {
		First = new ArrayList<String>();
	}
}
