package com.example.SpringSecurity.dto;

import com.example.SpringSecurity.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/*
✅Spring Security
1. 유저 로그인 시 UserDetailsService.loadUserByUserName(username) 실행
2. 반환된 UserDetails에서 비밀번호/권한 확인
3. 인증 완료 시 SecurityContext에 저장

따라서 (1) UserDetails를 구현한 사용자 정보 클래스 와 (2) UserDetailsSerivce 를 구현해서 사용자 정보를 DB에서 가져오는 서비스 둘을 요구함
 */

public class CustomUserDetails implements UserDetails {

    private UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    // 사용자의 특정 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    // TODO: 아래 4개 메소드 추후 처리 필요 (DB에 관련 필드 추가)
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
        return true;
    }
}
