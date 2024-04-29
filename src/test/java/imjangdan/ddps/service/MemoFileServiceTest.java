package imjangdan.ddps.service;

import imjangdan.ddps.common.exception.ResourceNotFoundException;
import imjangdan.ddps.dto.response.file.ResMemoFileDownloadDto;
import imjangdan.ddps.dto.response.file.ResMemoFileUploadDto;
import imjangdan.ddps.entity.Memo;
import imjangdan.ddps.entity.MemoFile;
import imjangdan.ddps.repository.MemoFileRepository;
import imjangdan.ddps.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class MemoFileServiceTest {

    @Mock
    private MemoRepository memoRepository;

    @Mock
    private MemoFileRepository memoFileRepository;

    @InjectMocks
    private MemoFileService memoFileService;

    public MemoFileServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void upload() throws IOException {
        // Mocking
        Long memoId = 1L;
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile multipartFile = org.mockito.Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getInputStream()).thenReturn(org.mockito.Mockito.mock(java.io.InputStream.class));
        multipartFiles.add(multipartFile);
        Memo memo = new Memo();
        memo.setId(memoId);
        when(memoRepository.findById(anyLong())).thenReturn(Optional.of(memo));
        MemoFile memoFile = new MemoFile();
        when(memoFileRepository.save(any(MemoFile.class))).thenReturn(memoFile);

        // Test
        List<ResMemoFileUploadDto> result = memoFileService.upload(memoId, multipartFiles);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void download() throws IOException {
        // Mocking
        Long memoFileId = 1L;
        MemoFile memoFile = new MemoFile();
        when(memoFileRepository.findById(anyLong())).thenReturn(Optional.of(memoFile));
        when(memoFile.getFilePath()).thenReturn("test.txt");
        when(memoFile.getFileType()).thenReturn("text/plain");

        // Test
        ResMemoFileDownloadDto result = memoFileService.download(memoFileId);

        // Assert
        assertNotNull(result);
    }

    @Test
    void download_ThrowsExceptionWhenFileNotFound() {
        // Mocking
        Long memoFileId = 1L;
        when(memoFileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test & Assert
        assertThrows(ResourceNotFoundException.class, () -> memoFileService.download(memoFileId));
    }

    @Test
    void delete() {
        // Mocking
        Long memoFileId = 1L;
        MemoFile memoFile = new MemoFile();
        when(memoFileRepository.findById(anyLong())).thenReturn(Optional.of(memoFile));

        // Test
        assertDoesNotThrow(() -> memoFileService.delete(memoFileId));
    }

    @Test
    void delete_ThrowsExceptionWhenFileNotFound() {
        // Mocking
        Long memoFileId = 1L;
        when(memoFileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test & Assert
        assertThrows(ResourceNotFoundException.class, () -> memoFileService.delete(memoFileId));
    }
}
