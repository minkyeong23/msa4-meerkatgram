package com.msa4meerkatgram.domain.post.requests;

import lombok.Builder;

@Builder
public record PostCreateRes(
    long id
) {
}
