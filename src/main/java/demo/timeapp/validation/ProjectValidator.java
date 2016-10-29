package demo.timeapp.validation;

import demo.timeapp.entity.Project;
import demo.timeapp.repository.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by dhval on 7/27/16.
 */

public class ProjectValidator implements ConstraintValidator<ProjectValid, Project> {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectValidator.class);
    ProjectRepository repository;

    public ProjectRepository getRepository() {
        return repository;
    }

    @Autowired
    public void setRepository(ProjectRepository repository) {
        this.repository = repository;
    }

    public boolean supports(Class<?> clazz) {
        return Project.class.isAssignableFrom(clazz);
    }


    public void validate(Object target, Errors errors) {
        if (target == null || errors.hasErrors())
            return;
        Project project = (Project) target;
        if (repository.findTopByName(project.getName()).isPresent()) {
            errors.rejectValue("name", "project.name.notunique", "project.name.notunique");
        }
        if (repository.findTopByNameAndClient(project.getName(), project.getClient()).isPresent()) {
            errors.rejectValue("client", "project.client.notunique", "project.client.notunique");
        }
    }

    @Override
    public void initialize(ProjectValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(Project project, ConstraintValidatorContext context) {
            LOG.warn(project.toString() + Arrays.toString(Thread.currentThread().getStackTrace()));
            if (project == null) {
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addPropertyNode("name").addConstraintViolation();
                return false;
            }
            if (repository == null)
                return true;
            String name = project.getName();
            String client = project.getClient();
            if (StringUtils.isBlank(name) || StringUtils.isBlank(client)) {
                context.buildConstraintViolationWithTemplate("project.name.notunique").addPropertyNode("name").addConstraintViolation();
                return false;
            }
            boolean isValid = true;
            Optional<Project> p1 = repository.findTopByName(name);
            Optional<Project> p2 = repository.findTopByNameAndClient(name, client);
            LOG.warn("" + p1 + p2);
            if (p1.isPresent() && p1.get().getId() != project.getId()) {
                context.buildConstraintViolationWithTemplate("project.name.notunique").addPropertyNode("name").addConstraintViolation();
                isValid = false;
            }
            if (p2.isPresent() && p2.get().getId() != project.getId()) {
                context.buildConstraintViolationWithTemplate("project.client.notunique").addPropertyNode("client").addConstraintViolation();
                isValid = false;
            }
            return isValid;
        }
}
