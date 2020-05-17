package it.fjacquin.ppmtool.services;

import it.fjacquin.ppmtool.domain.Backlog;
import it.fjacquin.ppmtool.domain.ProjectTask;
import it.fjacquin.ppmtool.repositories.BacklogRepository;
import it.fjacquin.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

     @Autowired
     private BacklogRepository backlogRepository;

     @Autowired
     private ProjectTaskRepository projectTaskRepository;

     public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

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
     }

}
