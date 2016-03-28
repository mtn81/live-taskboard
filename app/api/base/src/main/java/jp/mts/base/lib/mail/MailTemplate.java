package jp.mts.base.lib.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MailTemplate {
	private List<String> templateLines;
	
	public MailTemplate(String templatePath) {
		templateLines = new ArrayList<>();
		try (Scanner scanner = new Scanner(this.getClass().getResourceAsStream(templatePath))) {
			while(scanner.hasNextLine()) {
				templateLines.add(scanner.nextLine());
			}
		}
	}
	
	public String build(Map<String, Object> params) {
		StringBuilder result = new StringBuilder();
		templateLines.forEach(line -> {
			if (params == null) {
				result.append(line);
			} else {
				String resolvedLine = line;
				params.entrySet().forEach(p -> {
					resolvedLine.replaceAll("\\$\\{" + p.getKey() + "\\}", p.getValue().toString());
				});
				result.append(resolvedLine);
			}
		});
		return result.toString();
	}

}
