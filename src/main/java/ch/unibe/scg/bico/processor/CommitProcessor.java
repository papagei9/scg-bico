package ch.unibe.scg.bico.processor;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.bico.model.Commit;
import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.parser.IssueInfoHolder;
import ch.unibe.scg.bico.parser.IssueTrackerParser;

public class CommitProcessor implements ItemProcessor<Commit, CommitIssue> {
	
	private String urlPattern;
	
	public CommitProcessor(String urlPattern) {
		//if(project == null) throw new NullPointerException("project is null!");
		//urlPattern = project.getIssueTrackerUrlPattern();
		this.urlPattern = urlPattern;
		if(urlPattern == null) throw new NullPointerException("urlPattern is null");
	}

	@Override
	public CommitIssue process(Commit input) throws Exception {
		//if(input.getCommitIssue() != null) return input;
		CommitIssue issue = input.getCommitIssue();
		System.out.println("commit process: " + issue.getName());
		//System.out.println(input.toString());
		IssueTrackerParser itp;
		try {
			itp = new IssueTrackerParser(urlPattern);
			//itp.setIssue(input.getCommitIssue());
			//CommitIssue result = itp.parse();
			IssueInfoHolder result = itp.parse(issue);
			//if(result != null) input.setCommitIssue(result);
			if(result != null) {
				issue.setPriority(result.getPriority());
				issue.setType(result.getType());
			}
			//else input.setCommitIssue(input.getCommitIssue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return issue;
	}

	/*public void setProject(Project project) {
		this.project = project;
	}*/
	
	
}