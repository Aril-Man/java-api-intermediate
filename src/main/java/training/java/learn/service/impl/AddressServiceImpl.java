package training.java.learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import training.java.learn.dto.AddressResponse;
import training.java.learn.dto.ContactResponse;
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

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validation(request);
        Contact contact = contactRepository.findFirstByUser(user);

        Address address = new Address(UUID.randomUUID().toString(), request.getStreet(), request.getCity(), request.getProvince(), request.getContry(), request.getPostalCode());
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

    @Transactional(readOnly = true)
    public AddressResponse getCurrentAddress(User user) {
        Contact contact = contactRepository.findFirstByUser(user);
        Address address = addressRepository.findByContactId(contact.getId());

        return toAddressResponse(address);
    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .provice(address.getProvince())
                .street(address.getStreet())
                .build();
    }
}
