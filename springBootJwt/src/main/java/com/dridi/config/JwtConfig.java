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
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.AuthorizationEndpointConfig;
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
@Configuration
@EnableAuthorizationServer
public class JwtConfig  extends AuthorizationServerConfigurerAdapter{
	

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Value("$dridi.oauth.tokenTimeout:3600")
	private int expiration;
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
		convertor.setSigningKey(signingToken);
		return  convertor;
	}
	
	
	public void configure(AuthorizationServerEndpointsConfigurer configurer) throws Exception {
		TokenEnhancerChain enhancerChain=new TokenEnhancerChain();
		List list=Arrays.asList(jwtAccessTokenConverter);
		enhancerChain.setTokenEnhancers(list);
		configurer.tokenStore(tokenStore).accessTokenConverter(jwtAccessTokenConverter).tokenEnhancer(enhancerChain);
			configurer.authenticationManager(authenticationManager);	
			configurer.userDetailsService(userDetailsService);
	}
	
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
	//	clients.inMemory().withClient("ramzi").secret("secret").accessTokenValiditySeconds(expiration).scopes("read","write").authorizedGrantTypes("password","refresh_token").resourceIds("resource");

	clients.jdbc(dataSource);
	}
}
