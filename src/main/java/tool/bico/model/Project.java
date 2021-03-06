package tool.bico.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="projects")
public class Project {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String url;
	private Type type;
	private String branch;
	private String issueTrackerUrlPattern;
	
	// change metric
	private int changeMetricTimeWindow;
	private int changeMetricEveryCommits;
	private boolean changeMetricsExcludeBigCommits;
	private int sourceMetricEveryCommits;
	private boolean sourceMetricsExcludeBigCommits;
	private boolean szzMetricsExcludeBigCommits;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="project", orphanRemoval = true)
	@OrderBy("timestamp")
	private Set<Commit> commits;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="project", orphanRemoval = true)
	@OrderBy("id")
	private Set<BigCommit> bigCommits;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="project", orphanRemoval = true)
	private Set<CommitIssueAnalysis> commitIssueAnalysis;
	
	public enum Type {
		GITHUB("GitHub"), GIT("Git"), JIRA("Jira"), BUGZILLA("Bugzilla");
		
		private final String name;
		
		Type(String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
	}
	
	protected Project() {
		this.commits = new HashSet<Commit>();
		this.bigCommits = new HashSet<BigCommit>();
	}
	
	public Project(Type type) {
		this.type = type;
		this.commits = new HashSet<Commit>();
		this.bigCommits = new HashSet<BigCommit>();
	}
	
	public Project(String name, Type type) {
		this.name = name;
		this.type = type;
		this.commits = new HashSet<Commit>();
		this.bigCommits = new HashSet<BigCommit>();
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

	public Set<Commit> getCommits() {
		return commits;
	}

	public void addCommit(Commit commit) {
		commit.setProject(this);
		this.commits.add(commit);
	}
	
	public void removeCommit(Commit commit) {
		this.commits.remove(commit);
		commit.setProject(null);
	}
	
	public void addCommits(ArrayList<Commit> list) {
		list.forEach(c -> c.setProject(this));
		this.commits.addAll(list);
	}
	
	public Set<BigCommit> getBigCommits() {
		return bigCommits;
	}
	
	public void addCommit(BigCommit bigCommit) {
		bigCommit.setProject(this);
		this.bigCommits.add(bigCommit);
	}
	
	public void removeBigCommit(BigCommit bigCommit) {
		this.bigCommits.remove(bigCommit);
		bigCommit.setProject(null);
	}
	
	public void addBigCommits(List<BigCommit> toAdd) {
		toAdd.forEach(c -> c.setProject(this));
		this.bigCommits.addAll(toAdd);
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

	public void setIssueTrackerUrlPattern(String string) {
		issueTrackerUrlPattern = string;
	}
	
	public String getIssueTrackerUrlPattern() {
		return issueTrackerUrlPattern;
	}
	
	public Long getId() {
		return id;
	}
	
	public void cleanForProcessing() {
		/*commits.forEach(s -> {
			s.setProject(null);	
			s.getCommitIssues().forEach(i -> {
				i.removeCommit(s);
			});
			s.getFiles().forEach(f -> {
				f.setCommit(null);
			});
			s.getFiles().clear();
		});*/
		commits.forEach(s -> {
			s.setProject(null);
		});
		commits.clear();
	}
	
	public int getNumberOfCategorizedCommits() {
		int count = 0;
		for(Commit c : commits) {
			if(c.commitIssues.size() > 0) count++;
		}
		return count;
	}

	public int getChangeMetricEveryCommits() {
		return changeMetricEveryCommits;
	}

	public void setChangeMetricEveryCommits(int changeMetricEveryCommits) {
		this.changeMetricEveryCommits = changeMetricEveryCommits;
	}

	public int getChangeMetricTimeWindow() {
		return changeMetricTimeWindow;
	}

	public void setChangeMetricTimeWindow(int changeMetricTimeWindow) {
		this.changeMetricTimeWindow = changeMetricTimeWindow;
	}

	public int getSourceMetricEveryCommits() {
		return sourceMetricEveryCommits;
	}

	public void setSourceMetricEveryCommits(int sourceMetricEveryCommits) {
		this.sourceMetricEveryCommits = sourceMetricEveryCommits;
	}

	public void setSourceMetricsExcludeBigCommits(boolean excludeBigCommits) {
		this.sourceMetricsExcludeBigCommits = excludeBigCommits;		
	}
	
	public boolean getSourceMetricsExcludeBigCommits() {
		return this.sourceMetricsExcludeBigCommits;
	}
	
	public void setChangeMetricsExcludeBigCommits(boolean excludeBigCommits) {
		this.changeMetricsExcludeBigCommits = excludeBigCommits;
	}
	
	public boolean getChangeMetricsExcludeBigCommits() {
		return this.changeMetricsExcludeBigCommits;
	}

	public boolean getSzzMetricsExcludeBigCommits() {
		return szzMetricsExcludeBigCommits;
	}

	public void setSzzMetricsExcludeBigCommits(boolean szzMetricsExcludeBigCommits) {
		this.szzMetricsExcludeBigCommits = szzMetricsExcludeBigCommits;
	}
	
	public void removeAllBigCommits() {
		for(BigCommit b : bigCommits) {
			b.setProject(null);
		}
		bigCommits.clear();
	}
}
