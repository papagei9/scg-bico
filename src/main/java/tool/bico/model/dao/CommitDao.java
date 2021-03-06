package tool.bico.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tool.bico.model.Commit;
import tool.bico.model.CommitIssue;
import tool.bico.model.Project;

@Repository
public class CommitDao implements CommitDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(Commit commit) {
		em.persist(commit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Commit> findAll() {
		return em.createQuery("SELECT c FROM Commit c").getResultList();
	}
	
	@Override
	public Commit findById(Long id) {
		return em.find(Commit.class, id);
	}
	
	@Override
	public void delete(Commit commit) {
		em.remove(commit);
	}
	
	@Override
	public void update(Commit commit) {
		em.merge(commit);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Commit> getProjectCommits(Project project) {
		return em.createQuery("SELECT c from  Commit c WHERE c.project = :project_id")
		.setParameter("project_id", project).getResultList();
	}
	
	@Override
	public void flush() {
		em.flush();
	}

	@Override
	public Commit getCommitByProjectAndRef(Project project, String ref) {
		try {
			return (Commit) em.createQuery("SELECT c FROM Commit c WHERE c.ref = :ref AND c.project = :project")
					.setParameter("project", project)
					.setParameter("ref", ref).getSingleResult();
		} catch(NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommitIssue> getCommitIssuesByCommit(Commit c) {
		return em.createQuery("SELECT c from CommitIssue c LEFT JOIN c.commits i WHERE i = :commit")
				.setParameter("commit", c).getResultList();
	}
}