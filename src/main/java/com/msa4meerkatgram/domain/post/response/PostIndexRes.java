
package com.msa4meerkatgram.domain.post.response;

import com.msa4meerkatgram.domain.post.entities.Post;
import lombok.Builder;

import java.util.List;

@Builder
public record PostIndexRes (
        long total
        , boolean lastPage
        , List<PostWithUserRes> posts
){
    public static PostIndexRes from(long total, boolean lastPage, List<Post> posts) {
        return new PostIndexRes(
                total
                , lastPage
                , posts.stream().map(PostWithUserRes::from).toList()
        );
    }
}
