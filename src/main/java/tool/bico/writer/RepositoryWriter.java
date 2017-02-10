package tool.bico.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import tool.bico.model.Commit;
import tool.bico.model.service.CommitService;

public class RepositoryWriter implements ItemWriter<Commit> {

	private CommitService commitService;

	public RepositoryWriter(CommitService commitService) {
		this.commitService = commitService;
	}
	
	@Override
	public void write(List<? extends Commit> items) throws Exception {
		for(Commit commit : items) {
			commitService.add(commit);
		}
	}
}