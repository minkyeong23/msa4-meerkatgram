package com.msa4meerkatgram.domain.post.services;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.post.repositories.PostRepository;
import com.msa4meerkatgram.domain.post.requests.PostCreateReq;
import com.msa4meerkatgram.domain.post.response.PostWithUserRes;
import com.msa4meerkatgram.global.errors.custom.DeletedRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // public PostIndexRes index(PostIndexReq postIndexReq) {
    //     int page = postIndexReq.page() - 1;
    //
    //     Page<Post> postPage = postRepository.findAll(
    //             PageRequest.of(page, postIndexReq.limit())
    //     );
    //
    //     return PostIndexRes.builder()
    //             .total(postPage.getTotalElements())
    //             .lastPage(postPage.isLast())
    //             .posts(postPage.getContent())
    //             .build();
    // }

    public PostWithUserRes show(long id) {

        Post result = postRepository.findById(id)
                .orElseThrow(() ->
                        new DeletedRecordException("이미 삭제된 게시글입니다."));

        return PostWithUserRes.from(result);
    }

    public Long create(PostCreateReq req, Long loginUserId) {

        Post post = new Post();

        post.setContent(req.text());
        post.setImage(req.img());

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }
}
