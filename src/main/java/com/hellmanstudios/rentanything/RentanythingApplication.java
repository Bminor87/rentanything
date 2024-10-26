package com.hellmanstudios.rentanything;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.entities.User;
import com.hellmanstudios.rentanything.entities.Rental;
import com.hellmanstudios.rentanything.entities.Role;
import com.hellmanstudios.rentanything.repository.CategoryRepository;
import com.hellmanstudios.rentanything.repository.ItemRepository;
import com.hellmanstudios.rentanything.repository.UserRepository;
import com.hellmanstudios.rentanything.storage.StorageProperties;
import com.hellmanstudios.rentanything.storage.StorageService;
import com.hellmanstudios.rentanything.repository.RentalRepository;
import com.hellmanstudios.rentanything.repository.RoleRepository;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class RentanythingApplication {

	private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RentanythingApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			//storageService.deleteAll();
			try {
				storageService.init();
			} catch (Exception e) {
				log.error("Error initializing storage", e);
			}
			
		};
	}

	// @Bean
	// public CommandLineRunner demo(CategoryRepository categoryRepository, ItemRepository itemRepository, UserRepository userRepository, RentalRepository rentalRepository, RoleRepository roleRepository) {
	// 	return (args) -> {
			 
	// 		Role userRole = roleRepository.save(new Role("USER"));
	// 		Role adminRole = roleRepository.save(new Role("ADMIN"));

	// 		User user = userRepository.save(new User("user", "$2y$10$JWF.ho1LMyJx/6iUajlmB.unoynCy7IkS91upvZwXnpJ7YcnOG.1C", "user@rentanything.com", userRole));
	// 		User admin = userRepository.save(new User("admin", "$2y$10$PkY2eBIXqNpmF1vgdQv3q.gd1rmtDHnOw31Gf3USUncZk3XcIVOjW", "admin@rentanything.com", adminRole));

	// 		Category category1 = categoryRepository.save(new Category("Musical Instruments", "Instruments for gigs or a day in the studio"));
	// 		Category category2 = categoryRepository.save(new Category("Tools", "Tools for a project or a quick fix"));
	// 		Category category3 = categoryRepository.save(new Category("Costumes", "Costumes for a party or a play"));
	// 		Category category4 = categoryRepository.save(new Category("Electronic Devices", "Devices for a presentation or a movie night"));
	// 		Category category5 = categoryRepository.save(new Category("Games", "Games for a game night or a rainy day"));

	// 		Item guitar = itemRepository.save(new Item("Guitar", "A beautiful guitar", "image1.jpg", 10.0, category1));
	// 		itemRepository.save(new Item("Stradivari Violin", "A beautiful violin", "image2.jpg", 100.0, category1));
	// 		itemRepository.save(new Item("Steinway Piano", "A beautiful piano", "image3.jpg", 50.0, category1));
	// 		itemRepository.save(new Item("Drill", "A powerful drill", "image4.jpg", 5.0, category2));
	// 		itemRepository.save(new Item("Saw", "A powerful saw", "image5.jpg", 3.0, category2));
	// 		Item hammer = itemRepository.save(new Item("Hammer", "A powerful hammer", "image6.jpg", 2.0, category2));
	// 		itemRepository.save(new Item("Spiderman Costume", "Full Spiderman costume", "image7.jpg", 20.0, category3));
	// 		itemRepository.save(new Item("Batman Costume", "Full Batman costume", "image8.jpg", 20.0, category3));
	// 		itemRepository.save(new Item("Princess' Dress", "Wanna play snow white?", "image9.jpg", 20.0, category3));
	// 		itemRepository.save(new Item("Projector", "A Projector", "image10.jpg", 15.0, category4));
	// 		itemRepository.save(new Item("Camera", "A Camera", "image11.jpg", 10.0, category4));
	// 		Item chess = itemRepository.save(new Item("Chess", "A maple wood chess set", "image12.jpg", 2.0, category5));
	// 		itemRepository.save(new Item("Monopoly", "A monopoly game", "image13.jpg", 2.0, category5));
	// 		itemRepository.save(new Item("Playing Cards", "Playing Cards for poker etc.", "image14.jpg", 2.0, category5));

	// 	};
	// }

}
