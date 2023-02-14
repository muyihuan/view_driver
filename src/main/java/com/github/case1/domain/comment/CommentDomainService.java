package com.github.case1.domain.comment;

import com.github.case1.domain.comment.model.CommentModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 评论领域
 */
public class CommentDomainService {

    public Map<Long, List<CommentModel>> queryCommentList(List<Long> topicIds, Integer page, Integer count) {
        return Collections.emptyMap();
    }

    public Map<Long, Integer> queryCommentCount(List<Long> topicIds) {
        return Collections.emptyMap();
    }
}
