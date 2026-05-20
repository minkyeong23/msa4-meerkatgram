package com.msa4meerkatgram.domain.post.entities;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Post {
    private Long id;
    private Long user_id;
    private String content;
    private String image;
    private String createAt;
    private String updatedAt;
    private String deletedAt;
}
