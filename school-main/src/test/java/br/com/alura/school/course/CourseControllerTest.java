package br.com.alura.school.course;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.school.user.NewUserEnroll;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    void should_retrieve_course_by_code() throws Exception {
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));

        mockMvc.perform(get("/courses/java-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("java-1")))
                .andExpect(jsonPath("$.name", is("Java OO")))
                .andExpect(jsonPath("$.shortDescription", is("Java and O...")));
    }

    @Test
    void should_retrieve_all_courses() throws Exception {
        courseRepository.save(new Course("spring-1", "Spring Basics", "Spring Core and Spring MVC."));
        courseRepository.save(new Course("spring-2", "Spring Boot", "Spring Boot"));

        mockMvc.perform(get("/courses")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].code", is("spring-1")))
                .andExpect(jsonPath("$[0].name", is("Spring Basics")))
                .andExpect(jsonPath("$[0].shortDescription", is("Spring Cor...")))
                .andExpect(jsonPath("$[1].code", is("spring-2")))
                .andExpect(jsonPath("$[1].name", is("Spring Boot")))
                .andExpect(jsonPath("$[1].shortDescription", is("Spring Boot")));
    }

    @Test
    void should_add_new_course() throws Exception {
        NewCourseRequest newCourseRequest = new NewCourseRequest("java-2", "Java Collections", "Java Collections: Lists, Sets, Maps and more.");

        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newCourseRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-2"));
    }
    
    @Test
    void should_enroll_user() throws Exception {
    	courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
    	userRepository.save(new User("alex", "alex@email.com"));
    	NewUserEnroll newUser = new NewUserEnroll("alex");

        mockMvc.perform(post("/courses/java-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newUser)))
        		.andExpect(status().isCreated()); 		
    }
    
    @Test
    void should_retrieve_no_content() throws Exception {
    	courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
    	userRepository.save(new User("alex", "alex@email.com"));
    	    	
    	mockMvc.perform(get("/enroll/report")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNoContent()); 		
    }

    @Test
    void should_retrieve_user_with_enrolled_courses() throws Exception {
    	User user = new User("fabiano", "fabiano@email");
    	Course course = new Course("spring-1", "Spring Basics", "Spring Core and Spring MVC.");
    	 
    	user.courses.add(course);
    	course.users.add(user);
    	userRepository.save(user);
    	courseRepository.save(course);
    	 
        mockMvc.perform(get("/enroll/report")
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("fabiano@email.com")))
                .andExpect(jsonPath("$.quantidade_matriculas", is(1)));
    }

}