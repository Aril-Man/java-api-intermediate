package training.java.learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import training.java.learn.dto.AddressResponse;
import training.java.learn.dto.CreateAddressRequest;
import training.java.learn.entity.Address;
import training.java.learn.entity.Contact;
import training.java.learn.entity.User;
import training.java.learn.repository.AddressRepository;
import training.java.learn.repository.ContactRepository;
import training.java.learn.service.AddressService;
import training.java.learn.service.ValidationService;

import java.util.Optional;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validation(request);
        Contact contact = contactRepository.findFirstByUser(user);

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setCity(request.getCity());
        address.setCountry(request.getContry());
        address.setProvince(request.getProvince());
        address.setStreet(request.getStreet());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);

        addressRepository.save(address);

        return AddressResponse.builder()
                .city(address.getCity())
                .country(address.getCountry())
                .provice(address.getProvince())
                .street(address.getStreet())
                .postalCode(address.getPostalCode())
                .build();
    }
}
