package imjangdan.ddps.service;

import imjangdan.ddps.common.exception.ResourceNotFoundException;
import imjangdan.ddps.dto.request.comment.CommentDto;
import imjangdan.ddps.dto.response.comment.ResCommentDto;
import imjangdan.ddps.entity.Board;
import imjangdan.ddps.entity.Comment;
import imjangdan.ddps.entity.Member;
import imjangdan.ddps.repository.BoardRepository;
import imjangdan.ddps.repository.CommentRepository;
import imjangdan.ddps.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentService commentService;

    public CommentServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllComments() {
        // Mocking
        Pageable pageable = Pageable.unpaged();
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment());
        Page<Comment> commentPage = new PageImpl<>(commentList);
        when(commentRepository.findAllWithMemberAndBoard(any(), anyLong())).thenReturn(commentPage);

        // Test
        Page<ResCommentDto> result = commentService.getAllComments(pageable, 1L);

        // Assert
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void write() {
        // Mocking
        CommentDto writeDto = new CommentDto("Test content");
        Member member = new Member();
        member.setId(1L);
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(new Board()));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test
        ResCommentDto result = commentService.write(1L, member, writeDto);

        // Assert
        assertNotNull(result);
        assertEquals("Test content", result.getContent());
    }

    @Test
    void update() {
        // Mocking
        CommentDto updateDto = new CommentDto("Updated content");
        Comment comment = new Comment();
        when(commentRepository.findByIdWithMemberAndBoard(anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test
        ResCommentDto result = commentService.update(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
    }

    @Test
    void delete() {
        // Mocking
        Comment comment = new Comment();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // Test
        assertDoesNotThrow(() -> commentService.delete(1L));
    }

    @Test
    void delete_ThrowsExceptionWhenCommentNotFound() {
        // Mocking
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test & Assert
        assertThrows(ResourceNotFoundException.class, () -> commentService.delete(1L));
    }
}
