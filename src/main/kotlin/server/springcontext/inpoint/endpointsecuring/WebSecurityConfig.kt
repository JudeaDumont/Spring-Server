package server.springcontext.inpoint.endpointsecuring

import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowired

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        //Authenticates the session
        auth
                .inMemoryAuthentication()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        //Authenticates through login screen
        http
                .authorizeRequests()
                .antMatchers("/anonymous*")
                .anonymous()
        http
                .authorizeRequests()
                .antMatchers("/login*")
                .permitAll()
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
    }

    @Throws(Exception::class)
    public override fun configure(auth: AuthenticationManagerBuilder?) {
        //        authenticates using ldap server
        auth!!
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org")
                .and()
                .passwordCompare()
                .passwordEncoder(LdapShaPasswordEncoder())
                .passwordAttribute("userPassword")
    }
}