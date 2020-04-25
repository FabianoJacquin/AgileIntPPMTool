package it.fjacquin.ppmtool.web;

import it.fjacquin.ppmtool.domain.Project;
import it.fjacquin.ppmtool.services.MapValidationErrorService;
import it.fjacquin.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService errorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project,
                                                    BindingResult result){

        ResponseEntity<?> errorMap = errorService.mapValidationService(result);

        if(errorMap != null){
            return errorMap;
        }

        projectService.saveOrUpdateProject(project);
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);

    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId){

        Project project = projectService.findProjectByIdentifier(projectId.toUpperCase());

        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

}
 