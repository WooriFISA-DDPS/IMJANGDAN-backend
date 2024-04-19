package imjangdan.ddps.controller;

import imjangdan.ddps.service.MemoFileService;
import imjangdan.ddps.dto.response.file.ResMemoFileDownloadDto;
import imjangdan.ddps.dto.response.file.ResMemoFileUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/memo/{memoId}/file")
@RequiredArgsConstructor
public class MemoFileController {

	private final MemoFileService memoFileService;

	@PostMapping("/upload")
	public ResponseEntity<List<ResMemoFileUploadDto>> upload (
		@PathVariable Long memoId,
		@RequestParam("file") List<MultipartFile> files) throws IOException {
		List<ResMemoFileUploadDto> saveFile = memoFileService.upload(memoId, files);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveFile);
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> download (
		@RequestParam("fileId") Long fileId) throws IOException {
		ResMemoFileDownloadDto downloadDto = memoFileService.download(fileId);

		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.parseMediaType(downloadDto.getFileType()))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + downloadDto.getFilename() + "\"")
			.body(new ByteArrayResource(downloadDto.getContent()));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Long> delete (
		@RequestParam("fileId") Long fileId) {
		memoFileService.delete(fileId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
