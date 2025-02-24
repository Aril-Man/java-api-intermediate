package training.java.learn.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import training.java.learn.dto.AddressResponse;
import training.java.learn.dto.CreateAddressRequest;
import training.java.learn.dto.WebResponse;
import training.java.learn.entity.Address;
import training.java.learn.entity.Contact;
import training.java.learn.entity.User;
import training.java.learn.repository.AddressRepository;
import training.java.learn.repository.ContactRepository;
import training.java.learn.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

//    @BeforeEach
//    void setUp() {
//        addressRepository.deleteAll();
//    }

    @Test
    void createAddressSucess() throws Exception {

        User user = userRepository.findById("putri").orElseThrow();

        Contact contact = contactRepository.findFirstByUser(user);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setCity("jakarta");
        request.setStreet("JL. Setiabudi");
        request.setContry("Indonesia");
        request.setProvince("Jawa barat");
        request.setPostalCode("10234");
        request.setContactId(contact.getId());

        mockMvc.perform(
                post("/api/address/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokens")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getMessage());
            assertEquals("jakarta", response.getData().getCity());
        });
    }

    @Test
    void createAddressUnauthorized() throws Exception {

        User user = userRepository.findById("putri").orElseThrow();

        Contact contact = contactRepository.findFirstByUser(user);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setCity("jakarta");
        request.setStreet("JL. Setiabudi");
        request.setContry("Indonesia");
        request.setProvince("Jawa barat");
        request.setPostalCode("10234");
        request.setContactId(contact.getId());

        mockMvc.perform(
                post("/api/address/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "salahh")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getMessage());
        });
    }

    @Test
    void createAddressUserMissingRequired() throws Exception {

        User user = userRepository.findById("putri").orElseThrow();

        Contact contact = contactRepository.findFirstByUser(user);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setCity("jakarta");
        request.setStreet("Jl. setiabudi");
        request.setContry(null);
        request.setProvince("Jawa barat");
        request.setPostalCode("10234");
        request.setContactId(contact.getId());

        mockMvc.perform(
                post("/api/address/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokens")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getMessage());
        });
    }

    @Test
    void getCurrentAddressUnauthorized() throws Exception {
        User user = userRepository.findById("putri").orElseThrow();
        Contact contact = contactRepository.findFirstByUser(user);

        Address address = new Address();
        address.setContact(contact);
        address.setId(UUID.randomUUID().toString());
        address.setCountry("Indo");
        address.setCity("Jkt");
        address.setPostalCode("10102");

        addressRepository.save(address);

        mockMvc.perform(
                get("/api/address/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN" , "salahhh")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getMessage());
        });
    }
}
