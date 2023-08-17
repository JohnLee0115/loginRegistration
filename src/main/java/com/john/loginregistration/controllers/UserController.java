package com.john.loginregistration.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.john.loginregistration.models.LoginUser;
import com.john.loginregistration.models.User;
import com.john.loginregistration.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	public UserController() {
		
	}
	
	@GetMapping("/login/register")
	public String loginReg(@ModelAttribute("newUser") User user, @ModelAttribute("loginUser") LoginUser loginUser) {
		
		return "User/loginReg.jsp";
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("newUser") User user, BindingResult result, Model model, HttpSession session) {
		// reject value if email is taken
		if(userService.getUser(user.getEmail()) != null) {
			result.rejectValue("email", "Unique", "Email already in use!");
		}
		// reject if passwords dont match
		if(!user.getPassword().equals(user.getConfirm())) {
			result.rejectValue("password", "Match", "Confirm password must match");
		}
		if(result.hasErrors()) {
			model.addAttribute("loginUser", new LoginUser());
			return "User/loginReg.jsp";
		}
		
		User newUser = userService.registerUser(user);
		session.setAttribute("user_id", newUser.getId());
		
		return"redirect:/";
	}
	
	@PostMapping("/login")
	public String loginUser(@Valid @ModelAttribute("loginUser") LoginUser loginUser, BindingResult result, Model model, HttpSession session) {
		User loggingUser = userService.login(loginUser, result);
		if(result.hasErrors()) {
			model.addAttribute("newUser", new User());
			return "User/loginReg.jsp";
		}
		session.setAttribute("user_id", loggingUser.getId());
		return "redirect:/";
		
	}
	
	

}
