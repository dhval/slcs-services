package demo.timeapp;

import demo.timeapp.dto.FormUser;
import demo.timeapp.entity.Project;
import demo.timeapp.repository.ProjectRepository;
import demo.timeapp.repository.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by dhval on 9/21/16.
 */
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        try {
            userManager.createUser(new FormUser("Dhval", "Mudawal", "d.hvalm@gmail.com", "user", "test1234"));
        }
        catch (AppException e) {
            LOG.info("Creating default user.");
        }
        try {
            userManager.createUser(new FormUser("Pratibha", "Shastri", "pshastri@slcssolutions.net", "admin", "test1234"));
        }
        catch (AppException e) {
            LOG.info("Creating default approver.");
        }
        if (!projectRepository.findTopByNameAndClient("Default", "SLCS LLC").isPresent()) {
            LOG.info("Creating default project.");
            Project project = new Project();
            project.setName("Default");
            project.setClient("SLCS LLC");
            projectRepository.save(project);
        }

        return;
    }
}
