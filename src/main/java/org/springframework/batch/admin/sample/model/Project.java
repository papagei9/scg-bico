package org.springframework.batch.admin.sample.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Project {

	@Id
	private Long id;
	
	private String name;	
	private String url;
	private Type type;
	private String branch;
	private String issueTrackerUrlPattern;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Commit> commits;
	
	public enum Type {
		GITHUB("Github"), GIT("Git");
		
		private final String name;
		
		Type(String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
	}
	
	protected Project() {}
	
	public Project(Type type) {
		this.type = type;
		this.commits = new ArrayList<Commit>();
	}
	
	public Project(String name, Type type) {
		this.name = name;
		this.type = type;
		this.commits = new ArrayList<Commit>();
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Commit> getCommits() {
		return commits;
	}

	public void addCommit(Commit commit) {
		this.commits.add(commit);
	}
	
	public void removeCommit(Commit commit) {
		this.commits.remove(commit);
	}
	
	public void addCommits(ArrayList<Commit> list) {
		this.commits.addAll(list);
	}
	
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String toString() {
		return String.format("Project[id=%d, name='%s', type='%s', url='%s']",
				id, name, type, url);
	}
	
	/*public List<CommitIssue> getAllIssues() {
		List<CommitIssue> result = new ArrayList<CommitIssue>();
		for(Commit c : commits) {
			result.add(c.getCommitIssue());
		}
		return result;
	}*/

	public void setIssueTrackerUrlPattern(String string) {
		issueTrackerUrlPattern = string;
	}
	
	public String getIssueTrackerUrlPattern() {
		return issueTrackerUrlPattern;
	}
	
	public Long getId() {
		return id;
	}
	
}