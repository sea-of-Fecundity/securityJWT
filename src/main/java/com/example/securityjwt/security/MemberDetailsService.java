package com.example.securityjwt.security;


import com.example.securityjwt.domain.Member;
import com.example.securityjwt.exception.user.AddressNotFoundException;
import com.example.securityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String address) throws UsernameNotFoundException {

        Member member = userRepository.findByAddress(address)
                .orElseThrow(() -> new AddressNotFoundException("가입되지 않은 이메일 입니다."));

        return CustomUserDetails.builder()
                .address(member.getAddress())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
