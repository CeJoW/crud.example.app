package com.cejow.crud.example.app.service;

import com.cejow.crud.example.app.dao.UserRepository;
import com.cejow.crud.example.app.model.Role;
import com.cejow.crud.example.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("UserDetailServiceId")
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByLoginIs(login);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found!"));

        return user.map(UserDetailsInApp::new).get();
    }

    private static class UserDetailsInApp implements UserDetails {

        private final String userName;
        private final String password;
        private final boolean active;
        private final List<GrantedAuthority> authorities;

        public UserDetailsInApp(User user) {
            this.userName = user.getLogin();
            this.password = user.getPassword();
            this.active = user.isActive();
            this.authorities = user.getRoles()
                    .stream()
                    .map(Role::getRoleName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return userName;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return active;
        }
    }
}
