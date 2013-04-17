package org.ls.util;

import org.ls.entity.NonterminalCharacter;
import org.ls.entity.Production;
import org.ls.main.GrammerAnalysis;

public class OutputUtil {
	/**
	 * 打印从文件读到的产生式
	 */
	public static void printProductions() {
		System.out.println("产生式：");
		for (Production p : GrammerAnalysis.productions) {
			System.out.println("左部:" + p.getLeft() + " 右部:" + p.getRight());
		}
		System.out.println();
	}

	/**
	 * 打印文法符号
	 */
	public static void printCharacters() {
		System.out.println("非终结符：");
		System.out.println(GrammerAnalysis.nCharacters.keySet());
		System.out.println("终结符：");
		System.out.println(GrammerAnalysis.tCharacters.keySet());
		System.out.println('\n');
	}

	/**
	 * 打印每个文法符号的FIRST集
	 */
	public static void printFirst() {
		for (String s : GrammerAnalysis.tCharacters.keySet()) {
			System.out.print("FIRST(" + s + ")" + " = ");
			System.out.println(GrammerAnalysis.tCharacters.get(s).First);
		}
		System.out.println();
		for (String s : GrammerAnalysis.nCharacters.keySet()) {
			System.out.print("FIRST(" + s + ")" + " = ");
			System.out.println(GrammerAnalysis.nCharacters.get(s).First);
		}
		System.out.println();
	}

	/**
	 * 打印终结符的FOLLOW集
	 */
	public static void printFollow() {
		for (String s : GrammerAnalysis.nCharacters.keySet()) {
			System.out.print("FOLLOW(" + s + ")" + " = ");
			System.out.println(((NonterminalCharacter) GrammerAnalysis.nCharacters
					.get(s)).Follow);
		}
		System.out.println();
	}

	/**
	 * 打印每个产生式的Select集
	 */
	public static void printSelect() {
		for (Production production : GrammerAnalysis.productions) {
			System.out.print("SELECT( " + production.getLeft() + "->"
					+ production.getRight() + " ) = ");
			System.out.println(production.Select);
		}
		System.out.println();
	}

	/**
	 * 打印预测分析表
	 */
	public static void printForecastTable() {
		System.out.println("预测分析表:");
		for (String s : GrammerAnalysis.ForecastTable.keySet()) {
			System.out.println(s + ":");
			System.out.print("| ");
			for (String s1 : GrammerAnalysis.ForecastTable.get(s).keySet()) {
				System.out.print(s1 + " => "
						+ GrammerAnalysis.ForecastTable.get(s).get(s1) + " | ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
