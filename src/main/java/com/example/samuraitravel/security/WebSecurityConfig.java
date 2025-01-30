package com.example.samuraitravel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
 
 @Configuration
 @EnableWebSecurity
 @EnableMethodSecurity
public class WebSecurityConfig {
     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
         	.authorizeHttpRequests((requests) -> requests.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/").permitAll()  // すべてのユーザーにアクセスを許可するURL
                 //.requestMatchers("/admin/**").hasRole("ADMIN")  // 管理者にのみアクセスを許可するURL
         		 //.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**").permitAll()  // すべてのユーザーにアクセスを許可するURL   
       			//.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses").permitAll()  // すべてのユーザーにアクセスを許可するUR
       			//.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses", "/houses/{id}").permitAll()  // すべてのユーザーにアクセスを許可するURL
         		.requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses", "/houses/{id}", "/stripe/webhook").permitAll()  // すべてのユーザーにアクセスを許可するURL
         		.requestMatchers("/houses/{houseId}/review/{reviewId}/edit","/houses/{houseId}/review/{reviewId}/delete").hasAnyRole("GENERAL", "ADMIN") // ROLE_GENERALまたはROLE_ADMINを持つユーザーにレビューの編集を許可
     			.requestMatchers("/admin/**").hasRole("ADMIN")  // 管理者にのみアクセスを許可するURL
       			.anyRequest().authenticated()                   // 上記以外のURLはログインが必要（会員または管理者のどちらでもOK）
       			
             )
             .formLogin((form) -> form
                 .loginPage("/login")              // ログインページのURL
                 .loginProcessingUrl("/login")     // ログインフォームの送信先URL
                 .defaultSuccessUrl("/?loggedIn")  // ログイン成功時のリダイレクト先URL
                 .failureUrl("/login?error")       // ログイン失敗時のリダイレクト先URL
                 .permitAll()
             )
             .logout((logout) -> logout
                 .logoutSuccessUrl("/?loggedOut")  // ログアウト時のリダイレクト先URL
                 .permitAll()
             //);
            )
             .csrf().ignoringRequestMatchers("/stripe/webhook");
            
         return http.build();
     }
     
     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }
     
}