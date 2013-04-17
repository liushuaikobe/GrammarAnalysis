package org.ls.entity;

import java.util.ArrayList;
import java.util.List;

public class NonterminalCharacter extends MyCharacter {
	public List<String> Follow;
	public List<String> Sync;

	public NonterminalCharacter(String what) {
		super();
		this.what = what;
		Follow = new ArrayList<String>();
		Sync = new ArrayList<String>();
	}

	/**
	 * 根据一些启发式的方法设置该终结符的同步记号集合，即把该终结符的FOLLOW集、FIRST集加入Sync集
	 */
	public void setSync() {
		Sync.addAll(Follow); // 添加Follow集
		for (String s : First) { // 添加First集
			if (!Sync.contains(s)) {
				Sync.add(s);
			}
		}
	}
}
