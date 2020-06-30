package it.fjacquin.ppmtool.services;

import it.fjacquin.ppmtool.domain.Backlog;
import it.fjacquin.ppmtool.domain.ProjectTask;
import it.fjacquin.ppmtool.exceptions.ProjectNotFoundException;
import it.fjacquin.ppmtool.repositories.BacklogRepository;
import it.fjacquin.ppmtool.repositories.ProjectRepository;
import it.fjacquin.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

     @Autowired
     private BacklogRepository backlogRepository;

     @Autowired
     private ProjectTaskRepository projectTaskRepository;

     @Autowired
     private ProjectRepository projectRepository;

     @Autowired
     private ProjectService projectService;

     public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

               Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
               //backlogRepository.findByProjectIdentifier(projectIdentifier);

               projectTask.setBacklog(backlog);

               Integer BacklogSequence = backlog.getPTSequence();
               BacklogSequence++;

               backlog.setPTSequence(BacklogSequence);

               projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
               projectTask.setProjectIdentifier(projectIdentifier);

               //Con React bisognerà aggiungere anche projectTask.getPriority() = 0
               if ( projectTask.getPriority() == null || projectTask.getPriority() == 0 ){
                    projectTask.setPriority(3);
               }

               if (projectTask.getStatus() == "" || projectTask.getStatus() == null){
                    projectTask.setStatus("TO_DO");
               }

                return projectTaskRepository.save(projectTask);

     }

     public Iterable<ProjectTask> findBacklogById(String backlog_id, String username) {

          projectService.findProjectByIdentifier(backlog_id, username);

          return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
     }

     public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

          Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
          if(backlog == null){
               throw new ProjectNotFoundException("Project with ID: '" + backlog_id + "' does not exist");
          }

          ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
          if(projectTask == null){
               throw new ProjectNotFoundException("Project Task with ID: '" + pt_id + "' not found");
          }

          if(!projectTask.getProjectIdentifier().equals(backlog_id)){
               throw new ProjectNotFoundException("Project Task '" + pt_id + "' does not exist in project: '"+ backlog_id + "'");
          }

          return projectTask;

     }

     public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){

          ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
          projectTask = updatedTask;

          return projectTaskRepository.save(projectTask);

     }

     public void deletePTByProjectSequence(String backlog_id, String pt_id){
          ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

          projectTaskRepository.delete(projectTask);
     }
}
