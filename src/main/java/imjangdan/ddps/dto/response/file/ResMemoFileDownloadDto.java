package imjangdan.ddps.dto.response.file;

import imjangdan.ddps.entity.MemoFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResMemoFileDownloadDto {

	private String filename;
	private String fileType;
	private byte[] content;

	@Builder
	public ResMemoFileDownloadDto(String filename, String fileType, byte[] content) {
		this.filename = filename;
		this.fileType = fileType;
		this.content = content;
	}

	public static ResMemoFileDownloadDto fromMemoFileResource(MemoFile file, String contentType, byte[] content) {
		return ResMemoFileDownloadDto.builder()
			.filename(file.getOriginFileName())
			.fileType(contentType)
			.content(content)
			.build();
	}
}

