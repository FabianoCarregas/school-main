package br.com.alura.school.course;

import static java.lang.String.format;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.school.support.validation.ResourceNotFoundException;
import br.com.alura.school.user.NewUserEnroll;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserEnrolled;
import br.com.alura.school.user.UserRepository;

@RestController
class CourseController {

    private final CourseRepository courseRepository;

    private final UserRepository userRepository;
    
    CourseController(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> allCourses() {
    	List<Course> list = courseRepository.findAll();
    	List<CourseResponse> listResp= list.stream().map(x -> new CourseResponse(x)).collect(Collectors.toList());
        return ResponseEntity.ok(listResp);
    }

    @GetMapping("/courses/{code}")
    ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException(code));
        return ResponseEntity.ok(new CourseResponse(course));
    } 
    
    @GetMapping("/enroll/report")
    ResponseEntity<List<UserEnrolled>> enroledUsers() {
    	List<UserEnrolled> users = userRepository.countCoursesByEnrolledUser();   	
    	if (users.toArray().length < 1) {
    		throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    	}
    	return ResponseEntity.ok(users);
    }
      
    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        courseRepository.save(newCourseRequest.toEntity());
        URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
        return ResponseEntity.created(location).build();
    }
    
    @PostMapping("/courses/{code}/enroll")
    @ResponseStatus(code = HttpStatus.CREATED)
    ResponseEntity<Void> courseEnroll(@PathVariable("code") String code, @RequestBody @Valid NewUserEnroll userEnroll) {
    Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException(code));
    Optional<User> userOptional = userRepository.findByUsername(userEnroll.getUsername());
    User user = userOptional.get();
    user.getCourses().add(course);
    userRepository.save(user);
    return null;
    }
}
