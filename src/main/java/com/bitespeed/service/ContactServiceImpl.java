package com.bitespeed.service;

import com.bitespeed.dto.ContactDTO;
import com.bitespeed.entity.Contact;
import com.bitespeed.enums.LinkPrecedence;
import com.bitespeed.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
    
    @Override
    public ContactDTO identifyContact(ContactDTO contactDTO) {
        String email = contactDTO.getEmail();
        String phoneNumber = contactDTO.getPhoneNumber();

        // fetch the data by phonenumber 
        List<Contact> phoneNumberContacts = contactRepository.findByPhoneNumber(phoneNumber);

        // fetch the data by email  
        List<Contact> emailContacts = contactRepository.findByEmail(email);
        
         
        // adding whole contact at one place that is existingContacts.
        List<Contact> existingContacts = new ArrayList<>();
        if(phoneNumberContacts != null)  existingContacts.addAll(phoneNumberContacts);
        if(emailContacts != null) existingContacts.addAll(emailContacts);

        // If contact does not exist, create a new "primary" contact
        if (existingContacts.isEmpty()) {
        
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence(String.valueOf(LinkPrecedence.primary));
            newContact.setLinkedId(null);
            // save the new data in database
            newContact = contactRepository.save(newContact);
           
            // converting in to DTO 
            return convertToContactDTO(newContact, Collections.emptyList());          
        }
     // If contact or Email exists , find the primary contact and secondary contacts (if any)
        else if(phoneNumberContacts == null || emailContacts == null){
            Contact primaryContact = null;
            List<Contact> secondaryContacts = new ArrayList<>();

            for (Contact existingContact : existingContacts) {
                if ( String.valueOf(LinkPrecedence.primary).equals(existingContact.getLinkPrecedence())) {
                    primaryContact = existingContact;
                } else {
                    secondaryContacts.add(existingContact);
                }
            }

           
            // If primary contact is found, add the new secondary contact
            if (primaryContact != null) {
                Contact newSecondaryContact = new Contact();
                newSecondaryContact.setEmail(email);
                newSecondaryContact.setPhoneNumber(phoneNumber);
                newSecondaryContact.setLinkPrecedence(String.valueOf(LinkPrecedence.secondary));
              
                
                //  first addedd secondary record linked_Id 
                if(primaryContact.getLinkedId() == null)
                   newSecondaryContact.setLinkedId(primaryContact.getId());
               
                else if(primaryContact.getLinkedId() != null)
                    newSecondaryContact.setLinkedId(primaryContact.getLinkedId());               
                newSecondaryContact = contactRepository.save(newSecondaryContact);
                secondaryContacts.add(newSecondaryContact);
            }          
            return convertToContactDTO(primaryContact, secondaryContacts);
        } 
        // If both email and phoneNumber have primary contacts, make the latest added primary contact a secondary.
        else if (!emailContacts.isEmpty() && !phoneNumberContacts.isEmpty()
                && "primary".equals(emailContacts.get(0).getLinkPrecedence())
                && "primary".equals(phoneNumberContacts.get(0).getLinkPrecedence())) {

            Contact latestPrimaryByEmail = emailContacts.get(0);
            Contact latestPrimaryByPhoneNumber = phoneNumberContacts.get(0);            
            List<Contact> secondaryContacts = new ArrayList<>();
            
            // Compare the createdAt dates to determine the latest added contact
            if (latestPrimaryByPhoneNumber.getCreatedAt().isAfter(latestPrimaryByEmail.getCreatedAt())) {
                latestPrimaryByPhoneNumber.setLinkPrecedence(String.valueOf(LinkPrecedence.secondary));
                latestPrimaryByPhoneNumber.setLinkedId(latestPrimaryByEmail.getId());
                latestPrimaryByPhoneNumber = contactRepository.save(latestPrimaryByPhoneNumber);
                secondaryContacts.add(latestPrimaryByPhoneNumber);
                convertToContactDTO(latestPrimaryByPhoneNumber, secondaryContacts);
            } else {
                latestPrimaryByEmail.setLinkPrecedence(String.valueOf(LinkPrecedence.secondary));
                latestPrimaryByEmail.setLinkedId(latestPrimaryByPhoneNumber.getId());
                latestPrimaryByEmail = contactRepository.save(latestPrimaryByEmail);
                secondaryContacts.add(latestPrimaryByEmail);
            }
            // adding the both primary key and in the list.
            convertToContactDTO(latestPrimaryByPhoneNumber, secondaryContacts);
            return convertToContactDTO(latestPrimaryByEmail, secondaryContacts);
        }
   // if secondary addedd email or id will give then return exception. 
       throw new RuntimeException("Invalid Combination !! "); 
    }
 
    //// conver tot DTO... 
    private ContactDTO convertToContactDTO(Contact primaryContact, List<Contact> secondaryContacts) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setPrimaryContatctId(primaryContact.getId());

        /// set mails
		List<String> emails =  mergeEmails(primaryContact, secondaryContacts);
        LinkedHashSet<String> mailHashSet   = new LinkedHashSet<>();
        for(String string : emails) {
        	 mailHashSet.add(string);
        }
        if (!emails.isEmpty()) {
          contactDTO.setEmails(mailHashSet);
        }
     
        // set phone number 
		List<String> phoneNumbers =  mergePhoneNumbers(primaryContact, secondaryContacts);
		   LinkedHashSet<String> phoneHashSet   = new LinkedHashSet<>();
	        for(String string : phoneNumbers) {
	        	 phoneHashSet.add(string);
	        }
	      
         if (!phoneNumbers.isEmpty()) {
            contactDTO.setPhoneNumbers(phoneHashSet);
         }

        List<Long> secondaryContactIds = new ArrayList<>();
        for (Contact secondaryContact : secondaryContacts) {
            secondaryContactIds.add(secondaryContact.getId());
        }
        contactDTO.setSecondaryContactIds(secondaryContactIds);
        return contactDTO;
    }
    
  // merging emails in the list  
    private List<String> mergeEmails(Contact primaryContact, List<Contact> secondaryContacts) {
        List<String> emails = new ArrayList<>();
        if (primaryContact.getEmail() != null) {
            emails.add(primaryContact.getEmail());
        }

        for (Contact secondaryContact : secondaryContacts) {
            if (secondaryContact.getEmail() != null) {
                emails.add(secondaryContact.getEmail());
            }
        }
        return emails;
    }
 /// meging phone numbers in the list.
    private List<String> mergePhoneNumbers(Contact primaryContact, List<Contact> secondaryContacts) {
        List<String> phoneNumbers = new ArrayList<>();
        if (primaryContact.getPhoneNumber() != null) {
            phoneNumbers.add(primaryContact.getPhoneNumber());
        }

        for (Contact secondaryContact : secondaryContacts) {
            if (secondaryContact.getPhoneNumber() != null) {
                phoneNumbers.add(secondaryContact.getPhoneNumber());
            }
        }
        return  phoneNumbers;
    }
}
