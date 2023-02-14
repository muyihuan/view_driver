package com.github.case1.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanghuan
 */
@Getter
@AllArgsConstructor
public class UserModel {
    private final String gender;
    private final String username;
    private final String userIcon;
}
