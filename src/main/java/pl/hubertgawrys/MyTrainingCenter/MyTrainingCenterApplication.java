package pl.hubertgawrys.MyTrainingCenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

	@SpringBootApplication
	@EntityScan(
	basePackageClasses = {MyTrainingCenterApplication.class, Jsr310JpaConverters.class}
    )
public class MyTrainingCenterApplication {



	public static void main(String[] args) {
		SpringApplication.run(MyTrainingCenterApplication.class, args);
	}
}
