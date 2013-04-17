package org.ls.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.ls.entity.Production;
import org.ls.entity.Token;

public class FileAccessUtil {

	public static final String TOKEN_STRING = " -> ";
	public static final String SPLIT_STRING = " | ";
	public static final String REPLACE_STRING = " @ ";

	/**
	 * 从指定路径的文法文件中把产生式读出来
	 * 
	 * @param filePath
	 *            文法文件的路径
	 * @return 产生式对象的ArrayList
	 * @throws IOException
	 */
	public static ArrayList<Production> readProductionFromFile(String filePath)
			throws IOException {
		ArrayList<Production> productions = new ArrayList<Production>();
		File file = new File(filePath);

		if (!file.exists() || file.isDirectory()) {
			System.out.println("Error！File not exists or it's a directory!");
			throw new FileNotFoundException();
		}

		String currentProduction = "";
		String currentLeft = "";
		String currentRight = "";
		BufferedReader br = new BufferedReader(new FileReader(file));
		currentProduction = br.readLine();
		while (currentProduction != null) {
			if (currentProduction.charAt(0) == '#' || currentProduction.charAt(0) == '\n') {
				currentProduction = br.readLine();
				continue;
			}
			String[] tmp = currentProduction.split(TOKEN_STRING);
			currentLeft = tmp[0];
			currentRight = tmp[1];
			if (currentRight.contains(SPLIT_STRING)) { // 包含多个候选式
				currentRight = currentRight.replace(SPLIT_STRING,
						REPLACE_STRING);
				String[] candidates = currentRight.split(REPLACE_STRING);
				for (String s : candidates) {
					ArrayList<String> tmpRight = new ArrayList<String>();
					for (String s1 : s.split(" ")) {
						tmpRight.add(s1);
					}
					productions.add(new Production(currentLeft, tmpRight));
				}
			} else { // 右部只有一个候选式
				ArrayList<String> tmpRight = new ArrayList<String>();
				for (String s : currentRight.split(" ")) {
					tmpRight.add(s);
				}
				productions.add(new Production(currentLeft, tmpRight));
			}
			currentProduction = br.readLine();
		}
		br.close();
		return productions;
	}

	/**
	 * 从指定的路径读出要分析的Token序列
	 * 
	 * @param tokenPath
	 *            文件路径
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Token> readTokenFromFile(String tokenPath)
			throws IOException {
		ArrayList<Token> tokens = new ArrayList<Token>();
		File file = new File(tokenPath);

		if (!file.exists() || file.isDirectory()) {
			System.out.println("Error！File not exists or it's a directory!");
			throw new FileNotFoundException();
		}

		String currentToken = "";
		String currentSymbol = "";
		String currentValue = "";
		BufferedReader br = new BufferedReader(new FileReader(file));
		currentToken = br.readLine();
		while (currentToken != null) {
			String[] tmp = currentToken.split("@");
			currentSymbol = tmp[0];
			currentValue = tmp[1];
			tokens.add(new Token(currentSymbol, currentValue));
			currentToken = br.readLine();
		}
		br.close();
		return tokens;
	}

}
