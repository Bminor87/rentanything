package com.hellmanstudios.rentanything.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;

import com.hellmanstudios.rentanything.entities.User;
import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.entities.Rental;
import com.hellmanstudios.rentanything.entities.Role;

@SpringBootTest(properties = "spring.profiles.active=test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RentalRepositoryTest {
    
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User testUser;
    private User testAdmin;
    private Item testItem1;
    private Item testItem2;

    @BeforeEach
    public void setUp() {

        Role userRole = new Role("USER");
        Role adminRole = new Role("ADMIN");
        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        testUser = new User("testuser", BCrypt.hashpw("password", BCrypt.gensalt()), "testuser@testdomain.com", userRole);
        testAdmin = new User("testadmin", BCrypt.hashpw("password", BCrypt.gensalt()), "testadmin@testdomain.com", adminRole);
        userRepository.save(testUser);
        userRepository.save(testAdmin);

        Category category = new Category("Musical Instruments");
        categoryRepository.save(category);
        Category managedCategory = categoryRepository.findById(category.getId()).orElseThrow();

        testItem1 = new Item("Stradivari Cello", "A test cello", "image2.jpg", 90.0, managedCategory);
        testItem2 = new Item("Steingraeber & Söhne Grand Piano", "A test piano", "image3.jpg", 120.0, managedCategory);
        itemRepository.save(testItem1);
        itemRepository.save(testItem2);

        Rental rental1 = new Rental(testUser, testItem1, java.time.LocalDateTime.now(), null);
        Rental rental2 = new Rental(testUser, testItem2, java.time.LocalDateTime.now(), null);
        rentalRepository.save(rental1);
        rentalRepository.save(rental2);
    }

    @Test
    public void testFindByUserUsername() {
        List<Rental> rentals = rentalRepository.findByUserUsername("testuser");
        assertThat(rentals).isNotEmpty();
        assertThat(rentals.size()).isEqualTo(2);

        assertThat(rentals.get(0).getUser().getUsername()).isEqualTo("testuser");
        assertThat(rentals.get(0).getItem().getName()).isIn("Stradivari Cello", "Steingraeber & Söhne Grand Piano");
    }

    @AfterEach
    public void cleanUp() {
        rentalRepository.deleteAll();
        itemRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
    
}
