package com.prod.ib3.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.prod.ib3.entities.Item;
import com.prod.ib3.entities.Message;
import com.prod.ib3.entities.Newsteller;
import com.prod.ib3.services.AllService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AllController {

    @Autowired
    private AllService allService;

    // Páginas estáticas
    @GetMapping("/home")
    public String homePage() {
        return "index";
    }

    @GetMapping("/sobre")
    public String showAbout() {
        return "sobre";
    }

    @GetMapping("/contato")
    public String showContact() {
        return "contato";
    }

    // Páginas administrativas
    @GetMapping("/admin")
    public String showAdmin() {
        return "pagina-adm-home";
    }

    @GetMapping("/admin-newsletter")
    public String showNewsletter() {
        return "pagina-adm-newsletter";
    }

    @GetMapping("/admin-senha")
    public String showPassword() {
        return "pagina-senha";
    }

    // Exibir item único (por ID)
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Item> getItemDetailsById(@PathVariable Long id) {
        Item item = allService.getItem(id);
        return ResponseEntity.ok(item);
    }

    // Exibir todos os itens em JSON (API)
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<Item>> getAllItemsJson() {
        return ResponseEntity.ok(allService.getAll());
    }

    // Exibir itens na tela de admin
    @GetMapping("/items")
    public String showItems(Model model) {
        List<Item> items = allService.getAllItems();
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
        return "pagina-adm-produto";
    }

    // Adicionar item
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
        return "redirect:/items";
    }

    // Remover item
    @PostMapping("/remove/{id}")
    public String removeItem(@PathVariable Long id) {
        allService.deleteItemById(id);
        return "redirect:/items";
    }

    // Página pública do catálogo
    @GetMapping("/catalogo")
    public String getCatalog(Model model) {
        List<Item> items = allService.getAllItems();
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
        return "catalogo";
    }

    // Enviar mensagem de contato
    @PostMapping("/messages/add")
    public String addMessage(@RequestParam("name") String name,
                             @RequestParam("phone") String phone,
                             @RequestParam("email") String email,
                             @RequestParam("subject") String subject,
                             @RequestParam("message") String messageContent,
                             Model model) {
        try {
            Message message = new Message();
            message.setName(name);
            message.setPhone(phone);
            message.setEmail(email);
            message.setSubject(subject);
            message.setMessage(messageContent);
            allService.saveMessage(message);
            model.addAttribute("message", "Mensagem enviada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Falha ao enviar a mensagem. Por favor, tente novamente.");
        }
        return "contato";
    }

    // Exibir mensagens para admin
    @GetMapping("/messages")
    public String getMessages(Model model) {
        List<Message> messages = allService.getAllMessages();
        model.addAttribute("messages", messages);
        return "message-list";
    }

    // Enviar newsletter
    @PostMapping("/newsletter/send")
    public ResponseEntity<String> sendNewsletter(@RequestParam("subject") String subject,
                                                 @RequestParam("message") String message,
                                                 HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Você precisa estar logado para enviar uma newsletter.");
        }
        String result = allService.sendNewsletter(subject, message);
        return ResponseEntity.ok(result);
    }

    // Inscrição na newsletter
    @PostMapping("/newsletter/subscribe")
    public String subscribeToNewsletter(
            @RequestParam("email") String email,
            @RequestParam(value = "redirectPage", required = false) String redirectPage,
            Model model) {
        try {
            allService.addSubscriber(email);
            model.addAttribute("message", "Obrigado por se inscrever!");
        } catch (Exception e) {
            model.addAttribute("message", "Falha na inscrição. Por favor, tente novamente.");
        }
        if (redirectPage == null || redirectPage.isEmpty()) {
            redirectPage = "sobre"; // página padrão
        }
        return redirectPage;
    }


    // Listar newsletters
    @GetMapping("/view-newsletters")
    public String viewNewsletters(Model model) {
        List<Newsteller> newsletters = allService.getAllNewsletters();
        model.addAttribute("newsletters", newsletters);
        return "newsletter-list";
    }

    // Dashboard combinando mensagens e newsletters
    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        model.addAttribute("messages", allService.getAllMessages());
        model.addAttribute("newsletters", allService.getAllNewsletters());
        return "pagina-adm-newsletter";
    }
}
