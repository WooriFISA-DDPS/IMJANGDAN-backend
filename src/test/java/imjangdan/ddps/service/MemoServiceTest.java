package imjangdan.ddps.service;

import imjangdan.ddps.common.exception.ResourceNotFoundException;
import imjangdan.ddps.dto.request.memo.MemoUpdateDto;
import imjangdan.ddps.dto.request.memo.MemoWriteDto;
import imjangdan.ddps.dto.request.memo.MemoSearchData;
import imjangdan.ddps.dto.response.memo.ResMemoDetailsDto;
import imjangdan.ddps.dto.response.memo.ResMemoListDto;
import imjangdan.ddps.dto.response.memo.ResMemoWriteDto;
import imjangdan.ddps.entity.Memo;
import imjangdan.ddps.entity.Member;
import imjangdan.ddps.repository.MemoRepository;
import imjangdan.ddps.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoServiceTest {

    @Mock
    private MemoRepository memoRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemoService memoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllMemos() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Page<Memo> memosPage = new PageImpl<>(Collections.singletonList(new Memo()));
        when(memoRepository.findAllWithMemberAndComments(pageable)).thenReturn(memosPage);

        // Act
        Page<ResMemoListDto> result = memoService.getAllMemos(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void search_ByTitle() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        MemoSearchData searchData = new MemoSearchData("title", "content", "writerName");
        searchData.setTitle("testTitle");
        Page<Memo> memosPage = new PageImpl<>(Collections.singletonList(new Memo()));
        when(memoRepository.findAllTitleContaining(searchData.getTitle(), pageable)).thenReturn(memosPage);

        // Act
        Page<ResMemoListDto> result = memoService.search(searchData, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void search_ByContent() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        MemoSearchData searchData = new MemoSearchData("title", "content", "writerName");
        searchData.setContent("testContent");
        Page<Memo> memosPage = new PageImpl<>(Collections.singletonList(new Memo()));
        when(memoRepository.findAllContentContaining(searchData.getContent(), pageable)).thenReturn(memosPage);

        // Act
        Page<ResMemoListDto> result = memoService.search(searchData, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void search_ByWriterName() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        MemoSearchData searchData = new MemoSearchData("title", "content", "writerName");
        searchData.setWriterName("testWriterName");
        Page<Memo> memosPage = new PageImpl<>(Collections.singletonList(new Memo()));
        when(memoRepository.findAllUsernameContaining(searchData.getWriterName(), pageable)).thenReturn(memosPage);

        // Act
        Page<ResMemoListDto> result = memoService.search(searchData, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void write() {
        // Arrange
        MemoWriteDto memoWriteDto = new MemoWriteDto();
        Member member = new Member();
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(memoRepository.save(any(Memo.class))).thenAnswer(invocation -> {
            Memo memo = invocation.getArgument(0);
            memo.setId(1L);
            return memo;
        });

        // Act
        ResMemoWriteDto result = memoService.write(memoWriteDto, member);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getWriterName());
    }

    @Test
    void detail() {
        // Arrange
        long memoId = 1L;
        Memo memo = new Memo();
        when(memoRepository.findByIdWithMemberAndCommentsAndFiles(memoId)).thenReturn(Optional.of(memo));

        // Act
        ResMemoDetailsDto result = memoService.detail(memoId);

        // Assert
        assertNotNull(result);
    }

    @Test
    void update() {
        // Arrange
        long memoId = 1L;
        MemoUpdateDto memoUpdateDto = new MemoUpdateDto();
        Memo memo = new Memo();
        when(memoRepository.findByIdWithMemberAndCommentsAndFiles(memoId)).thenReturn(Optional.of(memo));

        // Act
        ResMemoDetailsDto result = memoService.update(memoId, memoUpdateDto);

        // Assert
        assertNotNull(result);
    }

    @Test
    void delete() {
        // Arrange
        long memoId = 1L;
        doNothing().when(memoRepository).deleteById(memoId);

        // Act & Assert
        assertDoesNotThrow(() -> memoService.delete(memoId));
    }
}
