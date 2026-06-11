package com.msa4meerkatgram.domain.post.requests;

import jakarta.validation.constraints.NotBlank;

public record PostCreateReq(
    @NotBlank(message = "게시글 내용은 필수 입력 항목 입니다.")
    String text,

    @NotBlank(message = "사진 업로드는 필수 입력 항목 입니다.")
    String img
) {
}
