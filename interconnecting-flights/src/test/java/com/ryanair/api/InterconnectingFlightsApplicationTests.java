package com.ryanair.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ryanair.apis.InterconnectingFlightsApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterconnectingFlightsApplication.class)
@WebAppConfiguration
public class InterconnectingFlightsApplicationTests {
	private static final String API_ENDPOINT = "/interconnections?departure=%s&arrival=%s&departureDateTime=%s&arrivalDateTime=%s";

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private LocalDateTime date;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		this.date = LocalDateTime.now();
	}

	@Test
	public void testCaseInsensitive() throws Exception {
		mockMvc.perform(get(String.format(API_ENDPOINT, "BgY", "pMo",
				date.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
				date.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))))).andExpect(status().isOk())
				.andExpect(content().contentType(contentType));
	}

	@Test
	public void testPatternIATACode() throws Exception {
		mockMvc.perform(get(String.format(API_ENDPOINT, "BG", "PM0",
				date.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
				date.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(contentType));
	}

	@Test
	public void testSameIATACodes() throws Exception {
		mockMvc.perform(get(String.format(API_ENDPOINT, "PMO", "PMO",
				date.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
				date.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))))
				.andExpect(status().isBadRequest()).andExpect(content().contentType(contentType));
	}

	@Test
	public void testSameDate() throws Exception {
		mockMvc.perform(get(String.format(API_ENDPOINT, "BGY", "PMO",
				date.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
				date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))))).andExpect(status().isBadRequest())
				.andExpect(content().contentType(contentType));
	}

	@Test
	public void testBadDateFormat() throws Exception {
		String url = String.format(API_ENDPOINT, "BGY", "PMO", date.plusHours(1).format(DateTimeFormatter.ISO_DATE),
				date.plusDays(1).format(DateTimeFormatter.ISO_DATE));
		System.out.println("URL: " + url);
		mockMvc.perform(get(url)).andExpect(status().isBadRequest()).andExpect(content().contentType(contentType));
	}

	@Test
	public void testDeltaTimeTooLarge() throws Exception {
		mockMvc.perform(get(String.format(API_ENDPOINT, "BGY", "PMO",
				date.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
				date.plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))))
				.andExpect(status().isRequestedRangeNotSatisfiable()).andExpect(content().contentType(contentType));
	}

	@Test
	public void testDeltaTimeTooSmall() throws Exception {
		mockMvc.perform(get(String.format(API_ENDPOINT, "BGY", "PMO",
				date.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
				date.plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))))
				.andExpect(status().isNotFound()).andExpect(content().contentType(contentType));
	}
}
