package com.prod.ib3.controller;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prod.ib3.entities.Item;
import com.prod.ib3.entities.ItemImage;
import com.prod.ib3.entities.Message;
import com.prod.ib3.entities.Newsteller;
import com.prod.ib3.services.AllService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Controller
public class AllController {

    @Autowired
    AllService allService;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/catalogo")
    public String showCatalog() {
        return "catalogo"; // The name of the Thymeleaf template (catalogo.html)
    }

    @GetMapping("/sobre")
    public String showAbout() {
        return "sobre"; // The name of the Thymeleaf template (catalogo.html)
    }

    @GetMapping("/contato")
    public String showContact() {
        return "contato"; // The name of the Thymeleaf template (catalogo.html)
    }

    @GetMapping("/admin")
    public String showAdmin() {
        return "pagina-adm-home"; // The name of the Thymeleaf template (catalogo.html)
    }

    @GetMapping("/admin-newsletter")
    public String showNewsletter() {
        return "pagina-adm-newsletter"; // The name of the Thymeleaf template (catalogo.html)
    }

    @GetMapping("/admin-senha")
    public String showPassword() {
        return "pagina-senha"; // The name of the Thymeleaf template (catalogo.html)
    }

    @GetMapping("/item")
    public String showItem2() {
        return "pagina-produto"; // The name of the Thymeleaf template (catalogo.html)
    }

    @GetMapping("/{id}")
    public HttpEntity<ResponseEntity> getItemDetailsById(@PathVariable Long id) {

        Item body = allService.getItem(id);

        return new HttpEntity<>(new ResponseEntity<>(body, null, HttpStatusCode.valueOf(200)));
    }

    @GetMapping("/all")
    public HttpEntity<ResponseEntity> getAllItems() {

        List<Item> body = allService.getAll();

        return new HttpEntity<>(new ResponseEntity<>(body, null, HttpStatusCode.valueOf(200)));
    }

    /*
     * @DeleteMapping("/{itemId}/delete")
     * public ResponseEntity<?> deleteItemById(@PathVariable Long id,
     * HttpServletRequest request) {
     * 
     * HttpSession session = request.getSession(false);
     * if (session != null) {
     * User authenticatedSeller = (User) session.getAttribute("user");
     * 
     * Boolean isItemDeleted;
     * try {
     * isItemDeleted = allService.deleteItem(id, authenticatedSeller);
     * if (isItemDeleted == true) {
     * return ResponseEntity.ok("Item deleted successfully");
     * } else {
     * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     * }
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * }
     * 
     * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
     * }
     */

    @GetMapping("/items")
    public String showItems(Model model) {
        List<Item> items = allService.getAllItems();

        // Convert images to Base64 strings and prepare data for the template
        List<Map<String, Object>> itemsWithImages = items.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("category", item.getCategory());
            map.put("description", item.getDescription());
            map.put("pricing", item.getPricing());
            map.put("imageBase64", Base64.getEncoder().encodeToString(item.getImage()));
            return map;
        }).toList();

        model.addAttribute("items", itemsWithImages);
        return "pagina-adm-produto"; // Thymeleaf template name
    }

    @PostMapping("/items/add")
    public String addItem(@RequestParam("name") String name,
            @RequestParam("image") MultipartFile image,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("pricing") Integer pricing) throws IOException {
        Item item = new Item();
        item.setName(name);
        item.setCategory(category);
        item.setDescription(description);
        item.setPricing(pricing);
        item.setImage(image.getBytes());

        allService.saveItem(item);

        return "redirect:/items"; // Redirect back to the item page
    }

    @PostMapping("/remove/{id}")
    public String removeItem(@PathVariable Long id) {
        allService.deleteItemById(id);
        return "redirect:/items";
    }

    @PostMapping("/newsletter/send")
    public ResponseEntity<String> sendNewsletter(@RequestParam("subject") String subject,
            @RequestParam("message") String message,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to send a newsletter.");
        }

        String result = allService.sendNewsletter(subject, message);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/newsletter/subscribe")
    public String subscribeToNewsletter(@RequestParam("email") String email, Model model) {
        try {
            // Call service to handle subscription logic
            allService.addSubscriber(email);

            // Add success message to the model
            model.addAttribute("message", "Obrigado por se inscrever!");
        } catch (Exception e) {
            // Add error message to the model
            model.addAttribute("message", "Falha na inscrição. Por favor, tente novamente.");
        }

        // Return the same page to display the message
        return "sobre"; // Replace with your actual template name
    }

    @GetMapping("/newsletters")
    public String getNewsletters(Model model) {
        List<Newsteller> newsletters = allService.getAllNewsletters();
        model.addAttribute("newsletters", newsletters);
        return "newsletter-list";
    }

    @GetMapping("/catalog")
    public String getCatalog(Model model) {
        List<Item> items = allService.getAllItems(); // Fetch all items from the database
        model.addAttribute("items", items);
        return "catalog"; // Corresponds to catalog.html
    }

    @GetMapping("/messages")
    public String getMessages(Model model) {
        List<Message> messages = allService.getAllMessages();
        model.addAttribute("messages", messages);
        return "message-list"; // Corresponds to the Thymeleaf template name (e.g., message-list.html)
    }

    @PostMapping("/messages/add")
    public String addMessage(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String messageContent,
            Model model) {
        try {
            // Create and save the message entity
            Message message = new Message();
            message.setName(name);
            message.setPhone(phone);
            message.setEmail(email);
            message.setSubject(subject);
            message.setMessage(messageContent);
            allService.saveMessage(message);

            // Add success message to the model
            model.addAttribute("message", "Mensagem enviada com sucesso!");
        } catch (Exception e) {
            // Add error message to the model
            model.addAttribute("message", "Falha ao enviar a mensagem. Por favor, tente novamente.");
        }

        // Return the template name (e.g., "contact")
        return "contato"; // Replace "contact" with the actual template name
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        // Fetch messages and newsletters
        List<Message> messages = allService.getAllMessages();
        List<Newsteller> newsletters = allService.getAllNewsletters();

        // Add both lists to the model
        model.addAttribute("messages", messages);
        model.addAttribute("newsletters", newsletters);

        // Return the template name
        return "pagina-adm-newsletter"; // Replace with your actual template name
    }

    @GetMapping("/view-newsletters")
    public String viewNewsletters(Model model) {
        List<Newsteller> newsletters = allService.getAllNewsletters();
        model.addAttribute("newsletters", newsletters);
        return "newsletter-list"; // Thymeleaf template name
    }

}
