package training.java.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import training.java.learn.dto.AddressResponse;
import training.java.learn.dto.CreateAddressRequest;
import training.java.learn.dto.WebResponse;
import training.java.learn.entity.User;
import training.java.learn.service.AddressService;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(
            path = "/api/address/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(User user, @RequestBody CreateAddressRequest request) {
        AddressResponse result = addressService.create(user, request);

        return WebResponse.<AddressResponse>builder()
                .data(result)
                .build();
    }

    @GetMapping(
            path = "/api/address/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> getCurrentAddress(User user) {
        AddressResponse result = addressService.getCurrentAddress(user);

        return WebResponse.<AddressResponse>builder().data(result).build();
    }

    @GetMapping(
            path = "/api/contact/{contact_id}/address/{address_id}"
    )
    public WebResponse<AddressResponse> getAddress(User user,
                                                   @PathVariable String contactId,
                                                   @PathVariable String addressId) {

        AddressResponse result = addressService.getAddress(contactId, addressId);

        return WebResponse.<AddressResponse>builder()
                .data(result)
                .build();
    }
}
