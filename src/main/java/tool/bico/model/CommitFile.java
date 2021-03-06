package tool.bico.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.jgit.diff.DiffEntry;

import tool.bico.analysis.CheckForSourceOrTestFile;
import tool.bico.model.CommitFile;

@Entity
@Table(name="commitfiles")
public class CommitFile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private int additions;
	
	private int deletions;
	
	@Deprecated
	@Transient
	private String fileName;
	
	private String oldPath;
	
	private String newPath;
	
	private String fileExtension;
	
	private boolean isTest;
	
	@Column(columnDefinition = "TEXT") // TEXT for PostgreSQL, MEDIUMTEXT for MySQL !
	private String patch;
	
	private ChangeType changeType;
	
	@ManyToOne
	@JoinColumn(name = "commit_id")
	private Commit commit;
	
	public enum ChangeType {
		/** Add a new file to the project */
		ADD,
		/** Modify an existing file in the project (content and/or mode) */
		MODIFY,
		/** Delete an existing file from the project */
		DELETE,
		/** Rename an existing file to a new location */
		RENAME,
		/** Copy an existing file to a new location, keeping the original */
		COPY;
	}

	public CommitFile() {
	}

	public void setAdditions(int additions) {
		this.additions = additions;
	}
	
	public int getAdditions() {
		return additions;
	}

	public int getDeletions() {
		return deletions;
	}

	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}

	@Deprecated
	public void setFilename(String filename) {
		this.fileName = filename;
	}
	
	@Deprecated
	public String getFilename() {
		return fileName;
	}

	public String getPatch() {
		return patch;
	}

	public void setPatch(String patch) {
		this.patch = patch;
	}

	public int getChanges() {
		return additions + deletions;
	}

	/**
	 * Get the old name associated with this file.
	 * <p>
	 * The meaning of the old name can differ depending on the semantic meaning
	 * of this patch:
	 * <ul>
	 * <li><i>file add</i>: always <code>/dev/null</code></li>
	 * <li><i>file modify</i>: always {@link #getNewPath()}</li>
	 * <li><i>file delete</i>: always the file being deleted</li>
	 * <li><i>file copy</i>: source file the copy originates from</li>
	 * <li><i>file rename</i>: source file the rename originates from</li>
	 * </ul>
	 *
	 * @return old name for this file.
	 */
	public String getOldPath() {
		return oldPath;
	}

	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}

	/**
	 * Get the new name associated with this file.
	 * <p>
	 * The meaning of the new name can differ depending on the semantic meaning
	 * of this patch:
	 * <ul>
	 * <li><i>file add</i>: always the file being created</li>
	 * <li><i>file modify</i>: always {@link #getOldPath()}</li>
	 * <li><i>file delete</i>: always <code>/dev/null</code></li>
	 * <li><i>file copy</i>: destination file the copy ends up at</li>
	 * <li><i>file rename</i>: destination file the rename ends up at</li>
	 * </ul>
	 *
	 * @return new name for this file.
	 */
	public String getNewPath() {
		return newPath;
	}

	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}
	
	public void setChangeType(DiffEntry.ChangeType changeType) {
		switch(changeType) {
			case ADD: setChangeType(ChangeType.ADD); break;
			case COPY: setChangeType(ChangeType.COPY); break;
			case DELETE: setChangeType(ChangeType.DELETE); break;
			case MODIFY: setChangeType(ChangeType.MODIFY); break;
			case RENAME: setChangeType(ChangeType.RENAME); break;
			default: setChangeType(ChangeType.MODIFY); break;
		}
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	
	public Long getId() {
		return id;
	}

	public String toString() {
		return String.format("CommitFile[id=%d, newPath='%s', additions='%d', deletions='%d', changes='%d']",
				id, newPath, additions, deletions, getChanges());
	}
	
	public boolean isSrc() {
		return (newPath != null && !newPath.equals("/dev/null")) ? CheckForSourceOrTestFile.check(newPath) : CheckForSourceOrTestFile.check(oldPath);
	}
	
	public boolean isTest() {
		return !isSrc();
	}
	
	public void setFileExtension(String ext) {
		this.fileExtension = ext;
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
	
	public void updateFileExtension() {
		String[] ext;
		String[] parts;
		if(newPath != null && !newPath.equals("/dev/null")) {
			parts = newPath.split("/");
			if(parts.length == 0 || parts == null) parts = new String[]{newPath};
		} else {
			parts = oldPath.split("/");
			if(parts.length == 0 || parts == null) parts = new String[]{oldPath};
		}
		if(parts.length > 0) {
			ext = parts[parts.length-1].split("\\.");
			setFileExtension(ext[ext.length-1]);
		}
		
		isTest = !isSrc();
	}
}