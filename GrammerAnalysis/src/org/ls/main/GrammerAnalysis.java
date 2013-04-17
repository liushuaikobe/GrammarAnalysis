package org.ls.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ls.entity.MyCharacter;
import org.ls.entity.Production;
import org.ls.entity.Token;
import org.ls.util.AlgorithmUtil;
import org.ls.util.FileAccessUtil;
import org.ls.util.OutputUtil;

public class GrammerAnalysis {

	/**
	 * 非终结符
	 */
	public static Map<String, MyCharacter> nCharacters = new HashMap<String, MyCharacter>();
	/**
	 * 终结符
	 */
	public static Map<String, MyCharacter> tCharacters = new HashMap<String, MyCharacter>();
	/**
	 * 产生式
	 */
	public static List<Production> productions;
	/**
	 * 预测分析表
	 */
	public static Map<String, HashMap<String, ArrayList<String>>> ForecastTable = new HashMap<String, HashMap<String, ArrayList<String>>>();
	/**
	 * 为了提高递归的效率, 如果某个终结符经过N步推导后能推出空串，则把这个非终结符放在这个集合中 相当于cache
	 */
	public static List<String> canLeadNullList = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			GrammerAnalysis.productions = FileAccessUtil
					.readProductionFromFile("H://current/grammer.txt");
			OutputUtil.printProductions();

			AlgorithmUtil.setCharacters();
			OutputUtil.printCharacters();

			for (String s : tCharacters.keySet()) {
				AlgorithmUtil.setFirstOfTCharacter(s);
			}
			for (String s : nCharacters.keySet()) {
				AlgorithmUtil.initFirstOfNCharacter(s);
			}
			while (AlgorithmUtil.haveChanged == true) {
				AlgorithmUtil.haveChanged = false;
				for (String s : nCharacters.keySet()) {
					AlgorithmUtil.setFirstSet(s);
				}
			}
			// 去除掉First集中的空字符串元素
			for (String s : nCharacters.keySet()) {
				nCharacters.get(s).First.remove("$");
			}
			OutputUtil.printFirst();

			AlgorithmUtil.initFollow();
			AlgorithmUtil.haveChanged = true;
			while (AlgorithmUtil.haveChanged == true) {
				AlgorithmUtil.haveChanged = false;
				AlgorithmUtil.setFollow();
			}
			OutputUtil.printFollow();

			AlgorithmUtil.setSelectForProductions();
			OutputUtil.printSelect();

			AlgorithmUtil.setForecastTable();
			OutputUtil.printForecastTable();

			AlgorithmUtil.setSync();

			// String prog = "i+i*i"; // 待分析的程序
			
			ArrayList<Token> tokens = FileAccessUtil.readTokenFromFile("H://current/token.txt");
			ArrayList<String> sentence = new ArrayList<String>();
			
			for (Token token : tokens) {
				sentence.add(token.getToken());
			}
			
			System.out.println("要分析的句子：\n " + sentence);
			System.out.println("LL(1)分析结果：");
			System.out.println("*******************************************************************\n");
			ArrayList<Production> result = AlgorithmUtil.Analysis(sentence, "program");
			// for (Production p : result) {
			// System.out.println(p.getLeft() + "->" + p.getRight());
			// }
			System.out.println();
			System.out.println("*******************************************************************");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
