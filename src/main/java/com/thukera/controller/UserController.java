package com.thukera.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thukera.model.messages.NotFoundException;
import com.thukera.model.forms.LoginForm;
import com.thukera.model.forms.SignUpForm;
import com.thukera.model.entities.Role;
import com.thukera.model.entities.User;
import com.thukera.model.enums.RoleName;
import com.thukera.repository.RoleRepository;
import com.thukera.repository.UserRepository;

import com.google.gson.Gson;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	Gson gson = new Gson();

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> listar() {
		return userRepository.findAll();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Optional<User> findById(@PathVariable Long id) throws Exception {
		Optional<User> response = userRepository.findById(id);

		if (!response.isPresent()) {
			throw new NotFoundException("Recurso não encontrado");
		} else {
			return response;
		}
	}

	@PutMapping("/{userid}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public String updateUser(@PathVariable("userid") long userId, @RequestBody SignUpForm user) {

		try {
			User usuario = userRepository.findByAgentId(userId);

			usuario.setCpf(user.getCpf());
			usuario.setName(user.getName());
			usuario.setUsername(user.getUsername());
			usuario.setEmail(user.getEmail());
			usuario.setPassword(encoder.encode(user.getPassword()));
			usuario.setAgentId(user.getAgentId());
			usuario.setStatus(user.getStatus());

			Set<String> strRoles = user.getRole();
			Set<Role> roles = new HashSet<>();

			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
					roles.add(adminRole);

					break;
				case "pm":
					Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
					roles.add(pmRole);

					break;
				default:
					Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
					roles.add(userRole);
				}
			});

			usuario.setRoles(roles);

			userRepository.save(usuario);

			return gson.toJson("OK");
		} catch (Exception e) {
			throw new NotFoundException("Recurso não encontrado: " + e);
		}
	}

	@PutMapping("/changepassword")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public String resetPassword(@RequestBody LoginForm user) {

		User usuario = userRepository.findByEmail(user.getUsername());
		if (usuario == null) {
			throw new NotFoundException("Recurso não encontrado");
		} else {
			usuario.setPassword(encoder.encode(user.getPassword()));
		}
		userRepository.save(usuario);
		return gson.toJson("OK");
	}

	@PutMapping("/forgotpassword")
	// @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public String forgotPassword(@RequestBody SignUpForm user) {

		User usuario = userRepository.findByEmail(user.getEmail());
		if (usuario == null) {
			throw new NotFoundException("Recurso não encontrado");
		} else {

			//

			//

		}
		return gson.toJson("OK");
	}

	@PutMapping("/updateUser")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateUser(@RequestBody SignUpForm user) {

		try {

			User usuario = new User(user.getCpf(), user.getName(), user.getUsername(), user.getEmail(),
					encoder.encode(user.getPassword()), user.getAgentId(), user.getStatus());

			usuario.setId(user.getId());

			Set<String> strRoles = user.getRole();
			Set<Role> roles = new HashSet<>();

			strRoles.forEach(role -> {

				Role adminRole = roleRepository.findByName(RoleName.getRole(role))
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: " + role + " Role não encontrada."));
				roles.add(adminRole);

			});
			usuario.setRoles(roles);
			userRepository.save(usuario);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Error! User " + user.getUsername() + " not updated!");
		}
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/deleteUser/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {

		try {

			userRepository.deleteById(id);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error! User not deleted!");

		}
		return ResponseEntity.ok().build();
	}

}
