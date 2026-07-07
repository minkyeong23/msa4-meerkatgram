package com.msa4meerkatgram.domain.post.requests;

import lombok.Builder;

import java.util.List;

@Builder
public record PostIndexRes(
    long total
    ,boolean lastPage
    ,List<PostIndexRes> posts
) {
}
