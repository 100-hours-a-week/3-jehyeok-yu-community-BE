package com.kakaotechbootcamp.community.comment.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponseDto {

    private List<CommentResponseDto> comments;
}
