package com.msa4meerkatgram.domain.post.controllers;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.post.requests.PostCreateReq;
import com.msa4meerkatgram.domain.post.requests.PostCreateRes;
import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.requests.PostIndexRes;
import com.msa4meerkatgram.domain.post.services.PostService;
import com.msa4meerkatgram.global.errors.custom.InvalidTokenException;
import com.msa4meerkatgram.global.responses.GlobalRes;
import com.msa4meerkatgram.global.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final JwtProvider jwtProvider;

    @GetMapping("/posts")
    public ResponseEntity<GlobalRes<PostIndexRes>> index(PostIndexReq postIndexReq) {
        PostIndexRes postIndexRes = postService.index(postIndexReq);

        return ResponseEntity.status(200).body(
            GlobalRes.<PostIndexRes>builder()
                .code("00")
                .message("정상처리")
                .data(postIndexRes)
                .build()
        );
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<GlobalRes<Post>> show(
        @Min(value = 1, message = "1이상 숫자만 허용합니다.") @PathVariable long id
    ) {
        Post result = postService.show(id);

        return ResponseEntity.status(200).body(
            GlobalRes.<Post>builder()
                .code("00")
                .message("게시글 상세 정상 처리")
                .data(result)
                .build()
        );
    }

    @PostMapping("/posts")
    public ResponseEntity<GlobalRes<PostCreateRes>> create(
        @Valid @RequestBody PostCreateReq postCreateReq,
        HttpServletRequest request
        ) {
        String token = jwtProvider.extractAccessToken(request)
            .orElseThrow(() -> new InvalidTokenException("인증 토큰이 누락되었거나 유효하지 않습니다."));

        Claims claims = jwtProvider.extractClaims(token);

        long loginUserId = Long.parseLong(claims.getSubject());

        long postId = postService.create(postCreateReq, loginUserId);

        return ResponseEntity.status(201).body(
            GlobalRes.<PostCreateRes>builder()
                .code("00")
                .message("게시글 작성 성공")
                .data(new PostCreateRes(postId))
                .build()
        );
    }
}
