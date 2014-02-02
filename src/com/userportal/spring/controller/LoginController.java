package com.userportal.spring.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.userportal.spring.form.Login;
import com.userportal.spring.form.User;
import com.userportal.spring.service.LoginService;
import com.userportal.spring.validator.LoginValidator;

@Controller
public class LoginController
{

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private LoginValidator loginValidator; 
	
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		System.out.println("we are in initbinder");
		binder.setValidator(loginValidator);
	}
	

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(@ModelAttribute("login")@Valid Login login,BindingResult result, Model model)
	{
		
		//System.out.println("We have entered into controller class");
		if(result.hasErrors())
		{
			if(result.hasFieldErrors("userId"))
			{
				model.addAttribute("userIdError", result.getFieldError("userId").getDefaultMessage());
				
			}
			if(result.hasFieldErrors("userPassword"))
			{
				model.addAttribute("userPasswordError", result.getFieldError("userPassword").getDefaultMessage());
			}
			return "index";
		}
		
		
		for(Login obj:loginService.login())
		{
			if(obj.getUserId().equals(login.getUserId()))
			{
				if(obj.getUserPassword().equals(login.getUserPassword()))
				{
					return "home";
				}
				else
				{
					model.addAttribute("userPasswordError", "Wrong Password");
					return "index";
				}
				
			}
			
		}
		model.addAttribute("userIdError", "Wrong User Id");
		model.addAttribute("userPasswordError", "Wrong Password");
		return "index";
	}
	
}
