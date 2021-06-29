package sg.edu.iss.caps.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.iss.caps.model.Course;
import sg.edu.iss.caps.model.Student_Course;
import sg.edu.iss.caps.model.User;
import sg.edu.iss.caps.service.interfaces.ICourse;
import sg.edu.iss.caps.service.interfaces.ILecturer;
import sg.edu.iss.caps.service.interfaces.IStudentCourse;
import sg.edu.iss.caps.service.interfaces.IUser;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {
	
	@Autowired ILecturer lecturerService;
	@Autowired ICourse courseService;
	@Autowired IStudentCourse scService;
	@Autowired IUser uService;
	
	@GetMapping("/profile")
	public String viewProfile() {
		return "Profile";
	}
	
	//View courses taught by lecturer
	@GetMapping("/courses/{id}")
	public String viewCoursesTaught(Model model, HttpSession session, User user, @PathVariable("id") Integer id) {
		System.out.println(id);
		session.getAttribute("user");
		model.addAttribute("courses", courseService.findCourseByLecturerId(id));
		model.addAttribute("id", id);
		return "lecturer/courses";
	}
	
	@GetMapping("/course-detail")
	public String viewCourseDetails() {
		return "lecturer/course-detail";
	}
	
	@GetMapping("/student-list")
	public String viewCourseStudentList(Model model, @Param("keyword") String keyword) {
		List<User> listUsers = lecturerService.listAll(keyword);
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("keyword", keyword);
		return "lecturer/student-list";
	}
	
	
	@GetMapping("/{cid}/grade-student-list")
	public String gradeStudentList(Model model, HttpSession session,@PathVariable("cid") int cid) {
		session.getAttribute("user");
		Course course = courseService.findCourseById(cid);
        model.addAttribute("course", course);
		List<Student_Course> students = scService.listStudentsGradesInCourse(course);
        model.addAttribute("students", students);
		return "lecturer/grade-student-list";
	}
	
	@GetMapping("/{cid}/grade-student-list/edit/{id}")
	public String editStudentGrade(@PathVariable("id") int id,@PathVariable("cid") int cid,Model model, HttpSession session) {
		Course course = courseService.findCourseById(cid);
        model.addAttribute("course", course);
		session.getAttribute("user");
		Student_Course selectedStudentCourse = scService.findStudentCourseById(id);
		
		model.addAttribute("selectedStudentCourse",selectedStudentCourse);
		
		return "lecturer/edit";
	}
	
	@PostMapping("/grade-student/save")
	public String saveGradeForm(@ModelAttribute("selectedStudentCourse") @Valid Student_Course selectedStudentCourse,BindingResult bindingResult,Model model) {
		System.out.println(selectedStudentCourse.getGrade());
		scService.editStudentsGradesInCourse(selectedStudentCourse);
		model.addAttribute("selectedStudentCourse",selectedStudentCourse);
		return "lecturer/grade-student-list";
	}
	
	 
	
	
	
	

}
