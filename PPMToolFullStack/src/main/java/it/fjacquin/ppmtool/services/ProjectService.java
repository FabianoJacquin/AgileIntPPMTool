package it.fjacquin.ppmtool.services;

import it.fjacquin.ppmtool.domain.Project;
import it.fjacquin.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

   @Autowired
    private ProjectRepository projectRepository;

     public Project saveOrUpdateProject(Project project){
         return  projectRepository.save(project);
     }

}
