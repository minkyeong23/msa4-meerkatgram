package com.msa4meerkatgram.domain.post.controllers;

import com.msa4meerkatgram.domain.post.requests.PostCreateReq;
import com.msa4meerkatgram.domain.post.requests.PostCreateRes;
import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.response.PostIndexRes;
import com.msa4meerkatgram.domain.post.response.PostWithUserRes;
import com.msa4meerkatgram.domain.post.services.PostService;
import com.msa4meerkatgram.global.annotations.openapi.ApiNotValidErrorResponse;
import com.msa4meerkatgram.global.errors.custom.InvalidTokenException;
import com.msa4meerkatgram.global.responses.GlobalRes;
import com.msa4meerkatgram.global.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시글 관련 API", description = "게시글 관련")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final JwtProvider jwtProvider;

    @ApiResponse(responseCode = "200", description = "게시글 목록 획득 성공")
    @ApiNotValidErrorResponse
    @GetMapping("/posts")
    public ResponseEntity<GlobalRes<PostIndexRes>> index(PostIndexReq postIndexReq) {
        return ResponseEntity.ok(GlobalRes.success(postService.index(postIndexReq)));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<GlobalRes<PostWithUserRes>> show(
            @Parameter(description = "게시글 번호", example = "1") @Min(value = 1, message = "1이상 숫자만 허용합니다.")
            long id
    ) {
        return ResponseEntity.ok(GlobalRes.success(postService.show(id)));
    }

//    @PostMapping("/posts")
//    public ResponseEntity<GlobalRes<PostCreateRes>> create(
//            @Valid @RequestBody PostCreateReq postCreateReq,
//            HttpServletRequest request
//    ) {
//
//        String token = jwtProvider.extractAccessToken(request)
//                .orElseThrow(() ->
//                        new InvalidTokenException("인증 토큰이 누락되었거나 유효하지 않습니다."));
//
//        Claims claims = jwtProvider.extractClaims(token);
//        Long loginUserId = Long.parseLong(claims.getSubject());
//
//        Long postId = postService.create(postCreateReq, loginUserId);
//
//        return ResponseEntity.status(201).body(
//                GlobalRes.<PostCreateRes>builder()
//                        .code("00")
//                        .message("게시글 작성 성공")
//                        .data(new PostCreateRes(postId))
//                        .build()
//        );
//    }
}
