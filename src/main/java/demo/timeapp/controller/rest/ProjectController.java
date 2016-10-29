package demo.timeapp.controller.rest;

import demo.timeapp.dto.CurrentUser;
import demo.timeapp.dto.FormUser;
import demo.timeapp.dto.ListItem;
import demo.timeapp.entity.Project;
import demo.timeapp.entity.User;
import demo.timeapp.entity.UserProject;
import demo.timeapp.repository.ProjectRepository;
import demo.timeapp.repository.UserProjectRepository;
import demo.timeapp.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by dhval on 7/25/16.
 */
@RestController
@RequestMapping("/public/project")
public class ProjectController extends AbstractController implements Rest {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Project> projects() {
        return repository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Project findProject(@PathVariable("id") Long id) {
        return repository.findById(id).orElse(null);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<ListItem> list() {
        Stream<Project> stream = StreamSupport.stream(repository.findAll().spliterator(), false);
        return stream.map(project -> (new ListItem(project.getLabel(), Long.toString(project.getId())))).collect(Collectors.toList());
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @Transactional
    public Iterable<User> findUsers(@ModelAttribute("user") CurrentUser user,
                                                       @RequestBody final Project project) {
        return repository.findUser(project.getName(), project.getClient());
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Map<String, String>> addUser(@ModelAttribute("user") CurrentUser user,
                                                       @RequestBody final FormUser form) {
        if (StringUtils.isBlank(form.getProjectId()) || StringUtils.isBlank(form.getEmail()))
            return msg(0, "invalid request");
        Optional<Project> p = repository.findById(Long.parseLong(form.getProjectId()));
        Optional<User> u = userRepository.findByEmail(form.getEmail());
        if (p.isPresent() && u.isPresent()) {
            userProjectRepository.deleteUsers(u.get().getId());
            UserProject userProject = new UserProject();
            userProject.setProject(p.get());
            userProject.setUser(u.get());
            userProjectRepository.save(userProject);
            return msg(0, "created");
        }
        return msg(0, "not found");
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Map<String, String>> save(@ModelAttribute("user") CurrentUser user,
                                                    @RequestBody @Valid final Project project) {
        LOG.info("Save Project - " + project.toString());
        repository.save(project);
        return msg(0, "created");
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<Map<String, String>> update(@ModelAttribute("user") CurrentUser user,
                                                      @RequestBody @Valid final Project project) {
         LOG.info("Update Project - " + project.toString());
        repository.save(project);
        return msg(0, "modified");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<Map<String, String>> delete(@ModelAttribute("user") CurrentUser user,
                                                      @RequestBody final Project project) {
        LOG.info("Delete Project - " + project.toString());
        Optional<Project> p = repository.findTopByNameAndClient(project.getName(), project.getClient());
        if (!p.isPresent())
            return msg(0, "not found");
        repository.delete(p.get().getId());
        return msg(0, "deleted");
    }

}
