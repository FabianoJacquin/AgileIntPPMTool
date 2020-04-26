package it.fjacquin.ppmtool.services;

import it.fjacquin.ppmtool.domain.Project;
import it.fjacquin.ppmtool.exceptions.ProjectIdException;
import it.fjacquin.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

   @Autowired
    private ProjectRepository projectRepository;

     public Project saveOrUpdateProject(Project project){

         try{
             project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
             return  projectRepository.save(project);
         } catch (Exception e){
             throw new ProjectIdException("Project ID '" +
                     project.getProjectIdentifier().toUpperCase() + "'already exist");
         }
     }

     public Project findProjectByIdentifier(String projectId){

         Project project = projectRepository.findByProjectIdentifier(projectId);

         if(project == null){
             throw new ProjectIdException("Project ID '" + projectId + "' does not exist");
         }

         return project;
     }

     public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
     }

}
