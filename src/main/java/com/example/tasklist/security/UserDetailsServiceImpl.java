package com.example.tasklist.security;

import com.example.tasklist.exception.NotFoundException;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        User user = userDao.getUserByUsername(username)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователя с таким id не существует"
                ));

        return new UserDetailsImpl(user);
    }
}
