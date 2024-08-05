package com.projekat.cinemaApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projekat.cinemaApp.exceptions.RoleAlreadyExistException;
import com.projekat.cinemaApp.exceptions.UserAlreadyExistsException;
import com.projekat.cinemaApp.model.Role;
import com.projekat.cinemaApp.model.User;
import com.projekat.cinemaApp.repository.RoleRepository;
import com.projekat.cinemaApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class RoleService implements IRoleService {
	
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Override
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Role createRole(Role theRole) {
		String roleName = "ROLE_" + theRole.getName().toUpperCase();
		Role role = new Role(roleName);
		//da li rola vec postoji
		if(roleRepository.existsByName(role)) {
			throw new RoleAlreadyExistException(theRole.getName() + " role already exists.");
		}
		return roleRepository.save(role);
	}

	@Override
	public void deleteRole(Long roleId) {
		//unakrsno brisanje, posto je veza vise-prema-vise
		this.removeAllUsersFromRole(roleId);
		roleRepository.deleteById(roleId);
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name).get();
	}

	@Override
	public User removeUserFromRole(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
        Optional<Role>  role = roleRepository.findById(roleId);
        if(role.isPresent() && role.get().getUsers().contains(user.get())) {
        	role.get().removeUserFromRole(user.get());
        	roleRepository.save(role.get());
        	return user.get();
        }
		throw new UsernameNotFoundException("User not found");
	}

	@Override
	public User assignRoleToUser(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
        Optional<Role>  role = roleRepository.findById(roleId);
        if(user.isPresent() && user.get().getRoles().contains(role.get())) {
        	throw new UserAlreadyExistsException(user.get().getFirstName() + " is already assigned to the " + role.get().getName()+" role.");
        }
		if(role.isPresent()) {
			role.get().assignRoleToUser(user.get());
			roleRepository.save(role.get());
		}
		return user.get();
	}

	@Override
	public Role removeAllUsersFromRole(Long roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		role.get().removeAllUsersFromRole();
		return roleRepository.save(role.get());
	}

}
