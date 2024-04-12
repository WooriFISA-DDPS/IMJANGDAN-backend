package imjangdan.ddps.repository;

import imjangdan.ddps.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<BoardFile, Long> {

}
