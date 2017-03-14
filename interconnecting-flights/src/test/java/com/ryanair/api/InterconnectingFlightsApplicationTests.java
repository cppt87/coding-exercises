package com.ryanair.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.ryanair.apis.InterconnectingFlightsApplication;
import com.ryanair.apis.services.IRyanairAPIsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterconnectingFlightsApplication.class)
@WebAppConfiguration
public class InterconnectingFlightsApplicationTests {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private IRyanairAPIsService service;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
    public void userNotFound() throws Exception {
        mockMvc.perform(post("/george/bookmarks/")
                .content(this.json(new Bookmark()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleBookmark() throws Exception {
        mockMvc.perform(get("/" + userName + "/bookmarks/"
                + this.bookmarkList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.bookmarkList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.uri", is("http://bookmark.com/1/" + userName)))
                .andExpect(jsonPath("$.description", is("A description")));
    }

    @Test
    public void readBookmarks() throws Exception {
        mockMvc.perform(get("/" + userName + "/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(this.bookmarkList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].uri", is("http://bookmark.com/1/" + userName)))
                .andExpect(jsonPath("$[0].description", is("A description")))
                .andExpect(jsonPath("$[1].id", is(this.bookmarkList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].uri", is("http://bookmark.com/2/" + userName)))
                .andExpect(jsonPath("$[1].description", is("A description")));
    }

    @Test
    public void createBookmark() throws Exception {
        String bookmarkJson = json(new Bookmark(
                this.account, "http://spring.io", "a bookmark to the best resource for Spring news and information"));

        this.mockMvc.perform(post("/" + userName + "/bookmarks")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
