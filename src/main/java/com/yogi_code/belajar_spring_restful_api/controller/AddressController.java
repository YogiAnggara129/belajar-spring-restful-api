package com.yogi_code.belajar_spring_restful_api.controller;

import com.yogi_code.belajar_spring_restful_api.entity.User;
import com.yogi_code.belajar_spring_restful_api.model.AddressResponse;
import com.yogi_code.belajar_spring_restful_api.model.CreateAddressRequest;
import com.yogi_code.belajar_spring_restful_api.model.UpdateAddressRequest;
import com.yogi_code.belajar_spring_restful_api.model.WebResponse;
import com.yogi_code.belajar_spring_restful_api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping(
            path = "/api/contacts/{contactId}/addresses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(User user,
                                               @PathVariable("contactId") String contactId,
                                               @RequestBody CreateAddressRequest request) {
        request.setContactId(contactId);
        final AddressResponse response = addressService.create(user, request);
        return WebResponse.<AddressResponse>builder().data(response).build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> get(User user,
                                              @PathVariable("contactId") String contactId,
                                              @PathVariable("addressId") String addressId) {
        final AddressResponse response = addressService.get(user, contactId, addressId);
        return WebResponse.<AddressResponse>builder().data(response).build();
    }

    @PutMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> update(User user,
                                               @PathVariable("contactId") String contactId,
                                               @PathVariable("addressId") String addressId,
                                               @RequestBody UpdateAddressRequest request) {
        request.setContactId(contactId);
        request.setAddressId(addressId);
        final AddressResponse response = addressService.update(user, request);
        return WebResponse.<AddressResponse>builder().data(response).build();
    }

    @DeleteMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public  WebResponse<String> delete(User user,
                                              @PathVariable("contactId") String contactId,
                                              @PathVariable("addressId") String addressId) {
        addressService.remove(user, contactId, addressId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressResponse>> list(User user,
                                                   @PathVariable("contactId") String contactId) {
        final List<AddressResponse> response = addressService.list(user, contactId);
        return WebResponse.<List<AddressResponse>>builder().data(response).build();
    }
}
