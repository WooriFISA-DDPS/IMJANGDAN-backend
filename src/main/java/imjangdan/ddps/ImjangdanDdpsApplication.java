package imjangdan.ddps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //BaseTimeEntity
@SpringBootApplication
public class ImjangdanDdpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImjangdanDdpsApplication.class, args);
    }

}
