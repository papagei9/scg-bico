package tool.bico.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import tool.bico.model.CommitIssue;
import tool.bico.model.Project;

@Repository
public class CommitIssueDao implements CommitIssueDaoInterface {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void persist(CommitIssue commitIssue) {
		em.persist(commitIssue);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommitIssue> findAll() {
		return em.createQuery("SELECT c FROM CommitIssue c").getResultList();
	}
	
	@Override
	public CommitIssue findById(Long id) {
		return em.find(CommitIssue.class, id);
	}
	
	@Override
	public void delete(CommitIssue commitIssue) {
		if(!em.contains(commitIssue)) {
			CommitIssue c  = em.merge(commitIssue);
			em.remove(c);
		} else {
			em.remove(commitIssue);
		}
	}
	
	@Override
	public void update(CommitIssue commitIssue) {
		em.merge(commitIssue);
	}

	@Override
	public void updateAll(List<? extends CommitIssue> commitIssues) {
		for(CommitIssue ci : commitIssues) {
			em.merge(ci);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CommitIssue> findAllByProject(Project project) {
		return em.createQuery("SELECT c from CommitIssue c LEFT JOIN c.commit i WHERE i.project = :project")
				.setParameter("project", project).getResultList();
	}
}