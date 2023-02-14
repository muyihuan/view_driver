package com.github.case1.domain.user;

import com.github.case1.domain.user.model.UserModel;
import com.github.case1.domain.user.model.UserSocialStatusInfo;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * user领域
 */
public class UserDomainService {

    public Map<String, UserModel> batchGetUsers(Set<String> uidList) {
        return Collections.emptyMap();
    }

    public Map<String, UserSocialStatusInfo> batchGetUserSocialStatusInfos(Set<String> uidList) {
        return Collections.emptyMap();
    }
}
