package ch.unibe.scg.bico.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.unibe.scg.bico.controller.analysis.CommitAnalyzer;
import ch.unibe.scg.bico.controller.analysis.ResultsContainer;
import ch.unibe.scg.bico.model.CommitIssue;
import ch.unibe.scg.bico.model.Project;
import ch.unibe.scg.bico.model.service.ProjectService;

@Controller
@RequestMapping("/projects/{pid}/analyze")
public class AnalysisController {
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view(Model model, @PathVariable("pid") Long pid) {
		Project project = projectService.findById(pid);
		
		Set<CommitIssue.Type> typeSet = new HashSet<>();
		typeSet.add(CommitIssue.Type.BUG);
		typeSet.add(CommitIssue.Type.FEATURE);
		typeSet.add(CommitIssue.Type.IMPROVEMENT);
		typeSet.add(CommitIssue.Type.REFACTOR);
		typeSet.add(CommitIssue.Type.DOCUMENTATION);
		
		CommitAnalyzer ca = new CommitAnalyzer(project, typeSet);
		ca.load();
		ca.analyze();
		Map<CommitIssue.Type, ResultsContainer> results = ca.getTypeResults();

		model.addAttribute("results", results);
		model.addAttribute("project", project);
		
		return new ModelAndView("projects/analysis/view", model.asMap());
	}
}
