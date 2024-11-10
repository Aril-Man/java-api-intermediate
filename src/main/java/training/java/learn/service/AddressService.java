package training.java.learn.service;

import org.springframework.stereotype.Service;
import training.java.learn.dto.AddressResponse;
import training.java.learn.dto.CreateAddressRequest;
import training.java.learn.entity.User;

@Service
public interface AddressService {
    AddressResponse create(User user, CreateAddressRequest request);
    AddressResponse getCurrentAddress(User user);
    AddressResponse getAddress(String contactId, String addressId);
}
