package it.fjacquin.ppmtool.services;

import it.fjacquin.ppmtool.domain.Backlog;
import it.fjacquin.ppmtool.domain.Project;
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

     public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

          try {
               Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
               projectTask.setBacklog(backlog);

               Integer BacklogSequence = backlog.getPTSequence();
               BacklogSequence++;

               backlog.setPTSequence(BacklogSequence);

               projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
               projectTask.setProjectIdentifier(projectIdentifier);

               //Con React bisognerà aggiungere anche projectTask.getPriority() = 0
               if (projectTask.getPriority() == null){
                    projectTask.setPriority(3);
               }

               if (projectTask.getStatus() == "" || projectTask.getStatus() == null){
                    projectTask.setStatus("TO_DO");
               }

               return projectTaskRepository.save(projectTask);
          } catch (Exception e){
               throw new ProjectNotFoundException("Project not Found");
          }


     }

     public Iterable<ProjectTask> findBacklogById(String backlog_id) {

          Project project = projectRepository.findByProjectIdentifier(backlog_id);

          if (project == null){
               throw new ProjectNotFoundException("Project with ID: '" + backlog_id + "' does not exists");
          }

          return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
     }
}
