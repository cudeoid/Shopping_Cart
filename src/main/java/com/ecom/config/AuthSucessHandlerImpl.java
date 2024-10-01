package com.ecom.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthSucessHandlerImpl implements AuthenticationSuccessHandler {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	@Lazy
	private UserService userService ;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		Set<String> roles = AuthorityUtils.authorityListToSet(authorities);

		String email = request.getParameter("username");

		UserDtls userDtls = userRepository.findByEmail(email);

		if(userDtls != null){
			userDtls.setRole(userDtls.getRole());
			userDtls.setIsEnable(true);
			userDtls.setAccountNonLocked(true);
			userDtls.setFailedAttempt(0);
			UserDtls saveUser = userService.updateUser(userDtls);
		}
//


		// Jadi amara authtication thik hela ... then if(role_admin) then ame taku /admin ku neba
		// if(role_customer) then redirect to home page .......
		if(roles.contains("ROLE_ADMIN"))
		{
			response.sendRedirect("/admin/");
		}else {
			response.sendRedirect("/");
		}
		
	}

}
