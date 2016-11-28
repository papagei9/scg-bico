package ch.unibe.scg.bico.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.unibe.scg.bico.model.Commit;

public class IssueStringParser {
	private Pattern regex;
	public IssueStringParser(String regex) {
		this.regex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}
	
	public void parse(List<Commit> list) {
		for(Commit c : list) {
			this.parse(c);
		}
	}
	
	public String parse(Commit commit) {
		return internalParse(commit.getMessage());
		//commit.initIssue(s);
	}
	
	public String parse(String commitMessage) {
		return internalParse(commitMessage);
	}
	
	private String internalParse(String commitMessage) {
		Matcher m = regex.matcher(commitMessage.trim());
		if(m.find()) {
			if(m.group(1) == null) System.err.println(commitMessage.split("\\r?\\n")[0]);
			return m.group(1).toUpperCase();
		} else {
			System.err.println("Can't parse issue "+commitMessage.split("\\r?\\n")[0]);
		}
		return null;
	}
	
	public List<String> parseAll(String commitMessage) {
		Matcher m = regex.matcher(commitMessage.trim());
		List<String> result = new ArrayList<String>();
		while(m.find()) {
			if(m.group() == null) System.err.println("general error: "+commitMessage.split("\\r?\\n")[0]);
			/*for(int i = 1; i <= m.groupCount(); i++) {
				if(m.group(i) != null) result.add(m.group(i));
			}*/
			result.add(m.group());
		}
		if(result.size() == 0) {
			System.err.println("Can't parse issue "+commitMessage.split("\\r?\\n")[0]);
		}
		return result;
	}
}
