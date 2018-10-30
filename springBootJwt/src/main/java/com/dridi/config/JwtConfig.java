package com.dridi.config;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
//import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.AuthorizationEndpointConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.dridi.service.UserService;
@Configuration
@EnableAuthorizationServer
public class JwtConfig  extends AuthorizationServerConfigurerAdapter{
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	//@Value("3600")
	private int expiration=3600;
	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	private String signingToken="jwtdemo123";
	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private DataSource dataSource;
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};
	
	@Bean
	public TokenStore tokStore() {
		return new JwtTokenStore(accessTokenConvertor());
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConvertor() {
		
		JwtAccessTokenConverter convertor=new JwtAccessTokenConverter();
		convertor.setSigningKey("jwtdemo123");
		return  convertor;
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer configurer) throws Exception {
	/*	TokenEnhancerChain enhancerChain=new TokenEnhancerChain();
		List list=Arrays.asList(jwtAccessTokenConverter);
		enhancerChain.setTokenEnhancers(list);
		configurer.tokenStore(tokenStore).accessTokenConverter(jwtAccessTokenConverter).tokenEnhancer(enhancerChain);
			configurer.authenticationManager(authenticationManager);	
			configurer.userDetailsService(userDetailsService);*/
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		List list=Arrays.asList(jwtAccessTokenConverter);
		enhancerChain.setTokenEnhancers(list);
		configurer.tokenStore(tokenStore)
        .accessTokenConverter(jwtAccessTokenConverter)
        .tokenEnhancer(enhancerChain);
		configurer.authenticationManager(authenticationManager);
		System.out.println("userService=="+userService);
		configurer.userDetailsService(userService);
	}
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
		/*	clients.inMemory().withClient("varun").secret("secret").accessTokenValiditySeconds(expiration)
		.scopes("read", "write").authorizedGrantTypes("password", "refresh_token").resourceIds("resource");*/
	clients.jdbc(dataSource);
	}
}
