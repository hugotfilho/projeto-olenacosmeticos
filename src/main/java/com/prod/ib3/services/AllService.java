package com.prod.ib3.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prod.ib3.entities.Item;
import com.prod.ib3.entities.ItemImage;
import com.prod.ib3.entities.Message;
import com.prod.ib3.entities.Newsteller;
import com.prod.ib3.repositories.ItemImageRepository;
import com.prod.ib3.repositories.ItemRepository;
import com.prod.ib3.repositories.MessageRepository;
import com.prod.ib3.repositories.NewstellerRepository;


import jakarta.transaction.Transactional;

@Service
public class AllService {

    // falta a lógica de login

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImageRepository imageRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    NewstellerRepository newstellerRepository;

    @Autowired
    JavaMailSender mailSender;

    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id).get();
    }

    public List<ItemImage> getImagesByItemId(Long itemId) {
        return imageRepository.findItemImagesById(itemId);
    }

    @Transactional
    public Item createItem(String name, String description, Integer pricing, MultipartFile image) throws IOException {
        // Create and save the item
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setPricing(pricing);
        item.setCreatedDate(LocalDate.now());
        Item savedItem = itemRepository.save(item);

        // Convert and save the single image
        ItemImage imageEntity = new ItemImage();
        imageEntity.setImage(image.getBytes());
        imageEntity.setItem(savedItem);
        imageRepository.save(imageEntity); // Save the single image

        return savedItem;
    }

    @Transactional
    public Boolean deleteItem(Long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        Item item = itemOptional.get();

        imageRepository.deleteByItemId(itemId);
        itemRepository.delete(item);

        return false;
    }

    public String sendNewsletter(String subject, String messageBody) {
        List<Newsteller> subscribers = newstellerRepository.findAll();

        if (subscribers.isEmpty()) {
            return "No subscribers to send the newsletter.";
        }

        // Send the email to each subscriber
        for (Newsteller subscriber : subscribers) {
            sendEmail(subscriber.getEmail(), subject, messageBody);
        }

        return "Newsletter sent to all subscribers!";
    }

    public String addSubscriber(String email) {
        Optional<Newsteller> existingSubscriber = newstellerRepository.findByEmail(email);

        if (existingSubscriber.isPresent()) {
            return "Failure";
        }

        Newsteller newSubscriber = new Newsteller(email);
        newstellerRepository.save(newSubscriber);

        return "Success";
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public void deleteItemById(Long id) {
        itemRepository.deleteById(id);
    }

    public List<Newsteller> getAllNewsletters() {
        return newstellerRepository.findAll();
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}

    /*@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "A user user with the username" + email + "doesnt exist"));

        return UserModel.build(user);
    }*/

