package com.hellmanstudios.rentanything.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.repository.ItemRepository;
import com.hellmanstudios.rentanything.repository.CategoryRepository;

@RunWith(SpringRunner.class)
public class ItemRestControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private ItemRestController itemRestController;

    @Test
    public void givenItems_whenGetItems_thenReturnJsonArray() throws Exception {

        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(10.0);
        item.setCategory(categoryRepository.findById(1L).get());
        itemRepository.save(item);

        

    }

}
