package org.ls.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.ls.entity.NonterminalCharacter;
import org.ls.entity.Production;
import org.ls.entity.TerminateCharacter;
import org.ls.main.GrammerAnalysis;

public class AlgorithmUtil {

	public static boolean haveChanged = true; // 标明各个First集是否变化

	/**
	 * 获得指定终结符的First集
	 * 
	 * @param X
	 */
	public static void setFirstOfTCharacter(String X) {
		GrammerAnalysis.tCharacters.get(X).First.add(X);
	}

	/**
	 * 初始化非终结符的First集
	 * 
	 * @param X
	 */
	public static void initFirstOfNCharacter(String X) {
		for (Production p : GrammerAnalysis.productions) {
			if (X.equals(p.getLeft())) {
				String firstCharOfR = p.getRight().get(0);
				// 如果右部的首符号是终结符
				if (isTCharacter(firstCharOfR)) {
					if (!GrammerAnalysis.nCharacters.get(X).First
							.contains(firstCharOfR)) {
						GrammerAnalysis.nCharacters.get(X).First
								.add(firstCharOfR);
					}
				}
			}
		}
	}

	/**
	 * 被重复执行，直到所有非终结符的First集都不变化为止，以获得指定非终结符character的FIRST集
	 * 
	 * @param X
	 */
	public static void setFirstSet(String X) {
		for (Production p : GrammerAnalysis.productions) {
			if (X.equals(p.getLeft())) {
				String firstCharOfR = p.getRight().get(0);
				if (isNCharacter(firstCharOfR)) {
					for (String s : GrammerAnalysis.nCharacters
							.get(firstCharOfR).First) {
						if (!GrammerAnalysis.nCharacters.get(X).First
								.contains(s)) {
							GrammerAnalysis.nCharacters.get(X).First.add(s);
							haveChanged = true;
						}
					}
				}
			}
		}
		for (Production p : GrammerAnalysis.productions) {
			if (X.equals(p.getLeft())) {
				String currentChar = "";
				for (int i = 0; i < p.getRight().size(); i++) {
					currentChar = p.getRight().get(i);
					if (canLeadNull(currentChar)) {
						for (String s : getFirstSet(currentChar)) {
							if (!GrammerAnalysis.nCharacters.get(X).First
									.contains(s)) {
								GrammerAnalysis.nCharacters.get(X).First.add(s);
								haveChanged = true;
							}
						}
					} else {
						break;
					}
				}
				for (String s : getFirstSet(currentChar)) {
					if (!GrammerAnalysis.nCharacters.get(X).First.contains(s)) {
						GrammerAnalysis.nCharacters.get(X).First.add(s);
						haveChanged = true;
					}
				}
			}
		}
	}

	/**
	 * 递归判断X可否推出空串
	 * 
	 * @param X
	 * @return
	 */
	public static boolean canLeadNull(String X) {
		// X是终结符，则X不可能推出空串
		if (isTCharacter(X)) {
			return false;
		}
		// X是非终结符
		else {
			// 查找Cache表
			if (GrammerAnalysis.canLeadNullList.contains(X)) {
				return true;
			}
			for (Production p : GrammerAnalysis.productions) {
				if (X.equals(p.getLeft())) {
					// 存在一个 X=>$ 的产生式，则说明X可以推出空串
					if ("$".equals(p.getRight().get(0))) {
						// 把X加入Cache
						GrammerAnalysis.canLeadNullList.add(X);
						return true;
					}
					// 当前产生式不是 X=>$ ，递归调用
					else {
						boolean flag = true;
						for (int i = 0; i < p.getRight().size(); i++) {
							// 当前产生式不能产生空串
							if (!canLeadNull(p.getRight().get(i))) {
								flag = false;
								break;
							}
						}
						if (flag == true) {
							// 把X加入Cache
							GrammerAnalysis.canLeadNullList.add(X);
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	/**
	 * 判断给定的字符串是否是非终结符
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNCharacter(String s) {
		return GrammerAnalysis.nCharacters.containsKey(s);
	}

	/**
	 * 判断给定的字符是否是终结符
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isTCharacter(String s) {
		return GrammerAnalysis.tCharacters.containsKey(s);
	}

	/**
	 * 获取指定字符的FIRST集
	 * 
	 * @param X
	 * @return
	 */
	public static ArrayList<String> getFirstSet(String X) {
		if (isNCharacter(X)) {
			return (ArrayList<String>) GrammerAnalysis.nCharacters.get(X).First;
		} else if (isTCharacter(X)) {
			return (ArrayList<String>) GrammerAnalysis.tCharacters.get(X).First;
		} else {
			return null;
		}
	}

	/**
	 * 获得某产生式右部子串的FIRST集
	 * 
	 * @param alpha
	 * @return
	 */
	public static ArrayList<String> getFirstSetOfCharSequence(
			ArrayList<String> alpha) {
		ArrayList<String> FirstSet = new ArrayList<String>();

		// 初始化工作
		if (alpha.size() > 0) {
			String initStr = alpha.get(0);
			for (String s : getFirstSet(initStr)) {
				if (!FirstSet.contains(s)) {
					FirstSet.add(s);
				}
			}
		}
		// 求FIRST集
		if (alpha.size() == 1) {
			for (String s : getFirstSet(alpha.get(0))) {
				if (!FirstSet.contains(s)) {
					FirstSet.add(s);
				}
			}
		} else {
			int k = 0;
			while (canLeadNull(alpha.get(k)) && k < alpha.size() - 1) {
				for (String s : getFirstSet(alpha.get(k + 1))) {
					if (!FirstSet.contains(s)) {
						FirstSet.add(s);
					}
				}
				System.out.println();
				k++;
			}
		}
		return FirstSet;
	}

	/**
	 * 初始化FOLLOW集，默认第一个产生式的左部是S（句子的开始，根）
	 */
	public static void initFollow() {
		((NonterminalCharacter) GrammerAnalysis.nCharacters
				.get(GrammerAnalysis.productions.get(0).getLeft())).Follow
				.add("#");
	}

	/**
	 * 设置非终结符A的FOLLOW集
	 */
	public static void setFollow() {
		for (Production production : GrammerAnalysis.productions) {
			String currentLeft = production.getLeft();
			ArrayList<String> currentRight = production.getRight();

			int len = currentRight.size();

			for (int i = 0; i < len; i++) {
				String currentChar = currentRight.get(i);
				if (isTCharacter(currentChar)) {
					continue;
				}
				if (i < len - 1) {
					ArrayList<String> rest = new ArrayList<String>(
							currentRight.subList(i + 1, len));
					ArrayList<String> firstSetOfRest = getFirstSetOfCharSequence(rest);
					for (String s : firstSetOfRest) {
						if (!((NonterminalCharacter) GrammerAnalysis.nCharacters
								.get(currentChar)).Follow.contains(s)) {
							((NonterminalCharacter) GrammerAnalysis.nCharacters
									.get(currentChar)).Follow.add(s);
							haveChanged = true;
						}
					}
					boolean flag = true;
					for (int j = 0; j < rest.size(); j++) {
						if (!canLeadNull(rest.get(j))) {
							flag = false;
						}
					}
					if (flag) {
						ArrayList<String> followSetOfLeft = (ArrayList<String>) ((NonterminalCharacter) GrammerAnalysis.nCharacters
								.get(currentLeft)).Follow;
						for (String s : followSetOfLeft) {
							if (!((NonterminalCharacter) GrammerAnalysis.nCharacters
									.get(currentChar)).Follow.contains(s)) {
								((NonterminalCharacter) GrammerAnalysis.nCharacters
										.get(currentChar)).Follow.add(s);
								haveChanged = true;
							}
						}
					}
				} else if (i == len - 1) {
					ArrayList<String> followSetOfLeft = (ArrayList<String>) ((NonterminalCharacter) GrammerAnalysis.nCharacters
							.get(currentLeft)).Follow;
					for (String s : followSetOfLeft) {
						if (!((NonterminalCharacter) GrammerAnalysis.nCharacters
								.get(currentChar)).Follow.contains(s)) {
							((NonterminalCharacter) GrammerAnalysis.nCharacters
									.get(currentChar)).Follow.add(s);
							haveChanged = true;
						}
					}
				}
			}
		}
	}

	/**
	 * 为产生式序列设置select集
	 */
	public static void setSelectForProductions() {
		for (Production production : GrammerAnalysis.productions) {
			if ("$".equals(production.getRight().get(0))) {
				for (String s : ((NonterminalCharacter) GrammerAnalysis.nCharacters
						.get(production.getLeft())).Follow) {
					if (!production.Select.contains(s)) {
						production.Select.add(s);
					}
				}
			} else {
				for (String s : getFirstSetOfCharSequence(production.getRight())) {
					if (!production.Select.contains(s)) {
						production.Select.add(s);
					}
				}
				boolean flag = true;
				for (int i = 0; i < production.getRight().size(); i++) {
					if (!canLeadNull(production.getRight().get(i))) {
						flag = false;
						break;
					}
				}
				if (flag) { // 右部能推出空
					for (String s : ((NonterminalCharacter) GrammerAnalysis.nCharacters
							.get(production.getLeft())).Follow) {
						if (!production.Select.contains(s)) {
							production.Select.add(s);
						}
					}
				}
			}
		}
	}

	/**
	 * 为每个非终结符设置同步记号集合
	 */
	public static void setSync() {
		for (String s : GrammerAnalysis.nCharacters.keySet()) {
			((NonterminalCharacter) GrammerAnalysis.nCharacters.get(s))
					.setSync();
		}
	}

	/**
	 * 根据每个产生式的Select集构造预测分析表
	 */
	public static void setForecastTable() {
		for (Production production : GrammerAnalysis.productions) {
			if (GrammerAnalysis.ForecastTable.keySet().contains(
					production.getLeft())) {
				for (String s : production.Select) {
					GrammerAnalysis.ForecastTable.get(production.getLeft())
							.put(s, production.getRight());
				}
			} else {
				HashMap<String, ArrayList<String>> rowMap = new HashMap<String, ArrayList<String>>();
				for (String s : production.Select) {
					rowMap.put(s, production.getRight());
				}
				GrammerAnalysis.ForecastTable.put(production.getLeft(), rowMap);
			}
		}
	}

	/**
	 * 根据当前文法分析句子，输出分析结果
	 * 
	 * @param sentence
	 *            要分析的语句（Token表示）
	 * @param startChar
	 *            当前文法的起始符号
	 * @return 返回自顶向下推导序列
	 */
	public static ArrayList<Production> Analysis(ArrayList<String> sentence,
			String startChar) {
		ArrayList<Production> productionSequences = new ArrayList<Production>();
		Stack<String> prodChars = new Stack<String>();
		prodChars.push("#");
		prodChars.push(startChar);
		// sentence = sentence + "#";
		sentence.add("#");
		int currentIndex = 0; // 当前分析到的下标

		while (!"#".equals(prodChars.peek())) {
			String X = prodChars.peek();
			String a = "";
			if (currentIndex < sentence.size()) {
				a = sentence.get(currentIndex);
			}
			if (isTCharacter(X) || "#".equals(X)) {
				if (a.equals(X)) {
					if (!"#".equals(X)) {
						prodChars.pop();
						currentIndex++;
					}
				} else {
					String eStr = prodChars.pop();
					System.err.println("ERROR,Ignore Char : " + eStr);
					// break;
				}
			} else {
				ArrayList<String> item = GrammerAnalysis.ForecastTable.get(X)
						.get(a);
				if (item != null) {
					prodChars.pop();
					if (!"$".equals(item.get(0))) {
						for (int i = item.size() - 1; i > -1; i--) {
							prodChars.push(item.get(i));
						}
					}
					productionSequences.add(new Production(X, item));
					System.out.println(X + " -> " + item);
				} else {
					if (((NonterminalCharacter) GrammerAnalysis.nCharacters
							.get(X)).Sync.contains(a)) {
						String eStr = prodChars.pop();
						System.err
								.println("ERROR,Have Pop NCharacter: " + eStr);
					} else {
						String eStr = a;
						System.err.println("ERROR,-Ignore Char : " + eStr);
						currentIndex++;
					}
					// break;
				}

			}
		}
		return productionSequences;
	}

	/**
	 * 解析产生式得到终结符和非终结符的集合
	 */
	public static void setCharacters() {
		// 将非终结符（出现在产生式左部的字符串）放入非终结符的Map中
		for (Production p : GrammerAnalysis.productions) {
			if (!GrammerAnalysis.nCharacters.containsKey(p.getLeft())) {
				GrammerAnalysis.nCharacters.put(p.getLeft(),
						new NonterminalCharacter(p.getLeft()));
			}
		}
		// 找出产生式右部的终结符，放入终结符的Map中
		for (Production p : GrammerAnalysis.productions) {
			ArrayList<String> tmpRight = new ArrayList<String>();
			tmpRight.addAll(p.getRight());
			for (String s : GrammerAnalysis.nCharacters.keySet()) {
				tmpRight.remove(s);
				tmpRight.remove(s);
			}
			for (int i = 0; i < tmpRight.size(); i++) {
				if (!GrammerAnalysis.tCharacters.containsKey(tmpRight.get(i))) {
					GrammerAnalysis.tCharacters.put(tmpRight.get(i),
							new TerminateCharacter(tmpRight.get(i)));
				}
			}
		}
	}
}