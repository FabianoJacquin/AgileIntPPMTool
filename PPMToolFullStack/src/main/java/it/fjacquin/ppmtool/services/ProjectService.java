package it.fjacquin.ppmtool.services;

import it.fjacquin.ppmtool.domain.Backlog;
import it.fjacquin.ppmtool.domain.Project;
import it.fjacquin.ppmtool.domain.User;
import it.fjacquin.ppmtool.exceptions.ProjectIdException;
import it.fjacquin.ppmtool.exceptions.ProjectNotFoundException;
import it.fjacquin.ppmtool.repositories.BacklogRepository;
import it.fjacquin.ppmtool.repositories.ProjectRepository;
import it.fjacquin.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

     public Project saveOrUpdateProject(Project project, String username){

         if(project.getId() != null){
             Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

             if(existingProject != null && (!existingProject.getProjectLeader().equals(username))){
                 throw new ProjectNotFoundException("Project not found in your account");
             } else if (existingProject == null){
                 throw new ProjectNotFoundException("Project with ID:'" + project.getProjectIdentifier() +
                         "' cannot be updated because it doesn't exist");
             }

         }

         try{

             User user = userRepository.findUserByUsername(username);

             project.setUser(user);
             project.setProjectLeader(user.getUsername() );

             project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

             if(project.getId() == null){
                 Backlog backlog = new Backlog();
                 project.setBacklog(backlog);
                 backlog.setProject(project);
                 backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
             }

             if(project.getId() != null){

                 Backlog backlog = backlogRepository.
                         findByProjectIdentifier(project.getProjectIdentifier().toUpperCase());

                 project.setBacklog(backlog);
             }

             return  projectRepository.save(project);
         } catch (Exception e){
             throw new ProjectIdException("Project ID '" +
                     project.getProjectIdentifier().toUpperCase() + "'already exist");
         }
     }

     public Project findProjectByIdentifier(String projectId, String username){

         Project project = projectRepository.findByProjectIdentifier(projectId);

         if(project == null){
             throw new ProjectIdException("Project ID '" + projectId + "' does not exist");
         }

         if (!project.getProjectLeader().equals(username)){
             throw new ProjectNotFoundException("Project not found in your account");
         }

             return project;
         }

     public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
     }

     public void deleteProjectByIdentifier(String projectId, String username){

         projectRepository.delete(findProjectByIdentifier(projectId, username));

     }

}
