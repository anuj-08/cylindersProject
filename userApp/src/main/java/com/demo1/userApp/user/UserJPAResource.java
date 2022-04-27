package com.demo1.userApp.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserJPAResource {

	@Autowired
	private UserDaoService service;


	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers()
	{
		return service.findAll();
		
	}

	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id)
	{
		User user = service.findOne(id);
		if(user==null)
			throw new UserNotFoundException("id-"+ id);
		
		EntityModel<User> model = EntityModel.of(user);
		
		WebMvcLinkBuilder linkToUsers = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		model.add(linkToUsers.withRel("all-users"));
		
		return model;
		
	}
	

@PostMapping("/jpa/users")
public ResponseEntity<Object> createUser(@Valid @RequestBody User user)
{
	User saveUser = service.save(user);
	
	
	URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saveUser.getId()).toUri();

	return ResponseEntity.created(location).build();
	
}

@DeleteMapping("/jpa/users/{id}")
public void deleteUser(@PathVariable int id)
{
	User user = service.deleteById(id);
	if(user==null)
		throw new UserNotFoundException("id-"+ id);
	
}
	
	
}
