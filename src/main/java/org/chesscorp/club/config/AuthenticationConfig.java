package org.chesscorp.club.config;

import org.chesscorp.club.model.Account;
import org.chesscorp.club.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@Profile("security")
public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    private UserDetailsService userDetailsService() {
        return username -> {
            Account account = accountRepository.getOne(username);
            if (account != null) {
                return new User(account.getUsername(), account.getPassword(), true, true, true, true,
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("could not find the user '" + username + "'");
            }
        };
    }
}
