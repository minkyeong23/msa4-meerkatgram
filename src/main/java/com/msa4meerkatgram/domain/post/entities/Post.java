package com.msa4meerkatgram.domain.post.entities;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private Long userId;
    private String content;
    private String image;
    private String createAt;
    private String updatedAt;
    private String deletedAt;
}
