package com.smartContactManager.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartContactManager.dao.ContactsRepository;
import com.smartContactManager.dao.UserRepository;
import com.smartContactManager.entities.Contact;
import com.smartContactManager.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ContactsRepository contactsRepo;

	@RequestMapping("/index")
	public String userDashborard() {
		return "user/user_dashboard";
	}
	
	@RequestMapping("/view_contacts")
	public String viewContacts(Model m, Principal p) {
		String userName = p.getName();
		User user = this.userRepo.getUsserByName(userName);
		int userId = user.getId() ;
		List<Contact> contacts =  this.contactsRepo.findContactsByUser(userId);
		m.addAttribute("contacts", contacts);
	
		
		return "user/view_contacts";
	}
	
	@RequestMapping("/contact-form")
	public String contactPage(Principal principal,Model model) {
		String userName= principal.getName();
		User user= userRepo.getUsserByName(userName);
		model.addAttribute("user",user);
		return "user/contact_form";
	}
	
	@PostMapping("/addContact")
	public String addContact(@ModelAttribute Contact contact,Principal principal,Model model) {
		String userName= principal.getName();
		User user= userRepo.getUsserByName(userName);
		Contact userContact = new Contact();
		contact.setUser(user);
		user.getContacts().add(contact);
		userRepo.save(user);
		model.addAttribute("contact", new Contact());
		model.addAttribute("user",user);
		return "user/contact_form";
	}
	
	@PostMapping("/updateContact/{cId}")
	public String updateContact(@PathVariable("cId") int cId,Model model, Principal p) {
		Contact contactInfo = this.contactsRepo.findById(cId);
		model.addAttribute("contactInfo", contactInfo);
		return "/user/update_contact";
	}
	
	@PostMapping("/updateContactInfo")
	public String updateContactInfo(@ModelAttribute Contact contact,Principal principal,Model model) {
		String userName= principal.getName();
		User user= userRepo.getUsserByName(userName);
		contact.setUser(user);
		if (contact.getcId() !=0) {
		    // Existing contact, update it
		    this.contactsRepo.save(contact);
		}
		
		return "redirect:/user/view_contacts";
		
	}
	@PostMapping("/deleteContact/{cId}")
	public String deleteContact(@PathVariable("cId") int cId,Model model, Principal principal) {
	    String userName = principal.getName();
	    User user= userRepo.getUsserByName(userName);
	    Contact contactInfo = this.contactsRepo.findById(cId);
	    contactInfo.setUser(user);
	    // Check if the contact exists for the specified user

	    if (contactInfo != null) {
	        // Delete the existing contact
	        contactsRepo.delete(contactInfo);
	    }
	    return "redirect:/user/view_contacts";
	}

}
