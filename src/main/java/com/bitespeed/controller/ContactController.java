// ContactController.java
package com.bitespeed.controller;

import com.bitespeed.dto.ContactDTO;
import com.bitespeed.dto.ContactDTOReturn;
import com.bitespeed.service.ContactService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identify")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Map<String, ContactDTOReturn>> identifyContact(@RequestBody ContactDTO contactDTO) {
    	// gettting the data of ContactDTO from service.
        ContactDTO identifiedContact = contactService.identifyContact(contactDTO);

         // making the format of output windows.         
        ContactDTOReturn dtoReturn =new  ContactDTOReturn();
        dtoReturn.setPrimaryContatctId(identifiedContact.getPrimaryContatctId());
        dtoReturn.setEmails(identifiedContact.getEmails());
        dtoReturn.setPhoneNumbers(identifiedContact.getPhoneNumbers());
        dtoReturn.setSecondaryContactIds(identifiedContact.getSecondaryContactIds());
        
        
         // mapping conatct object. 
        Map<String, ContactDTOReturn> response = new HashMap<>();
        response.put("contact",dtoReturn );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

