package com.smartContactManager.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartContactManager.dao.UserRepository;
import com.smartContactManager.entities.User;
import com.smartContactManager.helper.ObjectWrapper;
import com.smartContactManager.service.UserService;

import jakarta.validation.Valid;

@Controller
public class HomeController {
	private String fromSubId;
	private String toSubId;
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@RequestMapping("/")
	public String home() {
		return "home";
	}
	@RequestMapping("/about")
	public String about() {
		return "about";
	}
	
	@RequestMapping("/signin")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping("/trackingFields")
	public String trackingFields(Model model) {
        List<Long> allUserIds = userService.getAllUserIds();
        model.addAttribute("userIds", allUserIds);
        return "trackingFields"; 
    }
	@RequestMapping(value="do_register",method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,Model model) {
		List<String> emails= userService.getAllUserEmails();
		if (emails.contains(user.getEmail())) {
			 String errorMessageEmail = "Email already exists";
		     model.addAttribute("errorMessageEmail", errorMessageEmail);
		        return "signup";
		}
		else if (user.getRepeatPassword()!=null && user.getPassword()!=null && user.getPassword().equals(user.getRepeatPassword()) && !result.hasErrors()) {
			
			user.setAbout("Test");
			user.setName(user.getName());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRepeatPassword(passwordEncoder.encode(user.getRepeatPassword()));
			user.setRole("ROLE_USER");
			user.setEmail(user.getEmail());
			user.setContacts(new ArrayList<>());
			this.userRepo.save(user);
		System.out.println("User "+user);
		//model.addAttribute("user", new User());
		
		}
		else if (!user.getPassword().equals(user.getRepeatPassword())) {
	        // Password and repeat password do not match, add a custom error message
	        String errorMessage = "Passwords do not match";
	        model.addAttribute("errorMessage", errorMessage);
	        return "signup";
	    }
		else if (result.hasErrors()) {
			
			System.out.println(result);
			return "signup";
		}
		
		model.addAttribute("user", user);
		return "success";
	}
	@PostMapping("/view")
    public String view(@RequestParam(name="fromSubmission",required=false) Long fromSubmissionId,
    		@RequestParam(name="toSubmission",required=false) Long toSubmissionId,
    		Model model) {
           
		if (fromSubmissionId!=null && toSubmissionId!=null) {
            User fromUserData = userService.getUserDataById(fromSubmissionId);
            User toUserData = userService.getUserDataById(toSubmissionId);
            model.addAttribute("fromView",fromUserData);
            model.addAttribute("toView",toUserData);
            ObjectWrapper objectWrapper = new ObjectWrapper();
            objectWrapper.setFromView(fromUserData);
            objectWrapper.setToView(toUserData);
            model.addAttribute("objectWrapper", objectWrapper);
		}
		List<Long> allUserIds = userService.getAllUserIds();
        model.addAttribute("userIds", allUserIds);
        return "trackingFields"; // Replace with the name of your view template
    }
	public String getFromSubId() {
		return fromSubId;
	}
	public void setFromSubId(String fromSubId) {
		this.fromSubId = fromSubId;
	}
	public String getToSubId() {
		return toSubId;
	}
	public void setToSubId(String toSubId) {
		this.toSubId = toSubId;
	}
}
