package imjangdan.ddps.service;

import imjangdan.ddps.common.exception.ResourceNotFoundException;
import imjangdan.ddps.dto.response.file.ResFileDownloadDto;
import imjangdan.ddps.dto.response.file.ResFileUploadDto;
import imjangdan.ddps.entity.Board;
import imjangdan.ddps.entity.BoardFile;
import imjangdan.ddps.repository.BoardRepository;
import imjangdan.ddps.repository.FileRepository;
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

class FileServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    public FileServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void upload() throws IOException {
        // Mocking
        Long boardId = 1L;
        List<MultipartFile> multipartFiles = new ArrayList<>();
        MultipartFile multipartFile = org.mockito.Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getInputStream()).thenReturn(org.mockito.Mockito.mock(java.io.InputStream.class));
        multipartFiles.add(multipartFile);
        Board board = new Board();
        board.setId(boardId);
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(board));
        BoardFile boardFile = new BoardFile();
        when(fileRepository.save(any(BoardFile.class))).thenReturn(boardFile);

        // Test
        List<ResFileUploadDto> result = fileService.upload(boardId, multipartFiles);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void download() throws IOException {
        // Mocking
        Long fileId = 1L;
        BoardFile file = new BoardFile();
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));
        when(file.getFilePath()).thenReturn("test.txt");
        when(file.getFileType()).thenReturn("text/plain");

        // Test
        ResFileDownloadDto result = fileService.download(fileId);

        // Assert
        assertNotNull(result);
    }

    @Test
    void download_ThrowsExceptionWhenFileNotFound() {
        // Mocking
        Long fileId = 1L;
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test & Assert
        assertThrows(ResourceNotFoundException.class, () -> fileService.download(fileId));
    }

    @Test
    void delete() {
        // Mocking
        Long fileId = 1L;
        BoardFile file = new BoardFile();
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));

        // Test
        assertDoesNotThrow(() -> fileService.delete(fileId));
    }

    @Test
    void delete_ThrowsExceptionWhenFileNotFound() {
        // Mocking
        Long fileId = 1L;
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test & Assert
        assertThrows(ResourceNotFoundException.class, () -> fileService.delete(fileId));
    }
}
