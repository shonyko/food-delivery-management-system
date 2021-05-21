package org.alexk.presentation.api;


import lombok.val;
import org.alexk.business.DeliveryService;
import org.alexk.business.IDeliveryServiceProcessing;
import org.alexk.business.models.BaseProduct;
import org.alexk.business.models.CompositeProduct;
import org.alexk.business.models.viewModels.MenuItemViewModel;
import org.alexk.data.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminAPIController {
    IDeliveryServiceProcessing service = DeliveryService.instance;

    @GetMapping(value = {"", "/{id}"})
    public ModelAndView getAll(HttpSession session, Model model, @PathVariable(required = false) Integer id) {
        model.addAttribute("success", true);

        int idToFilter = -1;
        if(id != null) {
            idToFilter = id;

            val toSelect = ((CompositeProduct)service.getMenuItem(id)).getItems().stream()
                    .map(item -> new MenuItemViewModel(item.getId(), item))
                    .collect(Collectors.toList());

            model.addAttribute("toSelect", toSelect);
        }

        int finalIdToFilter = idToFilter;
        val items = service.getMenuItems().stream()
                .filter(x -> x.getId() != finalIdToFilter)
                .map(item -> new MenuItemViewModel(item.getId(), item))
                .collect(Collectors.toList());

        model.addAttribute("data", items);

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @PostMapping("/import")
    public ModelAndView importProducts(HttpSession session, Model model, @RequestParam(value = "file", required = false) MultipartFile file) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Produsele au fost importate cu succes!");

        try {
            val products = FileReader.getProducts(file.getInputStream());
            service.importItems(products);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Importarea produselor a esuat!");
            System.out.println(e.getMessage());
        }

        model.asMap().remove("file");
        return new ModelAndView(new MappingJackson2JsonView());
    }

    /// BASE
    @GetMapping("/base")
    public ModelAndView getBase(HttpSession session, Model model) {
        model.addAttribute("success", true);

        val menuItems = service.getMenuItems();
        val items = menuItems.stream()
                .filter(x -> x instanceof BaseProduct)
                .map(item -> new MenuItemViewModel(item.getId(), item))
                .collect(Collectors.toList());

        model.addAttribute("data", items);

        return new ModelAndView(new MappingJackson2JsonView());
    }
    @PostMapping("/base")
    public ModelAndView addBase(HttpSession session, Model model, BaseProduct product) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Adaugarea a fost facuta cu succes!");

        if(service.containsProduct(product.getTitle())) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Exista deja un produs cu aceast nume!");
        }
        else {
            service.addMenuItem(product.updateId());
        }

        model.asMap().remove("baseProduct");
        return new ModelAndView(new MappingJackson2JsonView());
    }
    @PutMapping("/base/{id}")
    public ModelAndView updateBase(HttpSession session, Model model, BaseProduct product, @PathVariable int id) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Editarea a fost facuta cu succes!");

        service.updateMenuItem(id, product.setId(id));

        model.asMap().remove("baseProduct");
        return new ModelAndView(new MappingJackson2JsonView());
    }
    @DeleteMapping("/base/{id}")
    public ModelAndView deleteBase(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Stergerea a fost facuta cu succes!");

        service.removeMenuItem(id);

        return new ModelAndView(new MappingJackson2JsonView());
    }

    /// COMPOSITE
    @GetMapping("/composite")
    public ModelAndView getComposite(HttpSession session, Model model) {
        model.addAttribute("success", true);

        val menuItems = service.getMenuItems();
        val items = menuItems.stream()
                .filter(x -> x instanceof CompositeProduct)
                .map(item -> new MenuItemViewModel(item.getId(), item))
                .collect(Collectors.toList());

        model.addAttribute("data", items);

        return new ModelAndView(new MappingJackson2JsonView());
    }
    @PostMapping("/composite")
    public ModelAndView addComposite(HttpSession session, Model model, String title, double rating, int[] baseProducts) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Adaugarea a fost facuta cu succes!");

        if(service.containsProduct(title)) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Exista deja un produs cu aceast nume!");
        }
        else if(baseProducts == null || baseProducts.length <= 0) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Trebuie introdus cel putin un produs!");
        }
        else {
            val products = Arrays.stream(baseProducts)
                    .mapToObj(id -> service.getMenuItem(id))
                    .filter(x -> x != null)
                    .collect(Collectors.toList());

            service.addMenuItem(new CompositeProduct(title, rating, products));
        }

        model.asMap().remove("title");
        model.asMap().remove("rating");
        model.asMap().remove("baseProducts");
        return new ModelAndView(new MappingJackson2JsonView());
    }
    @PutMapping("/composite/{id}")
    public ModelAndView updateComposite(HttpSession session, Model model, String title, double rating, int[] baseProducts, @PathVariable int id) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Editarea a fost facuta cu succes!");

        if(baseProducts.length <= 0) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Trebuie introdus cel putin un produs!");
        }
        else {
            val products = Arrays.stream(baseProducts)
                    .mapToObj(_id -> service.getMenuItem(_id))
                    .filter(x -> x != null)
                    .collect(Collectors.toList());

            val product = new CompositeProduct();
            product.setId(id);
            product.setTitle(title);
            product.setRating(rating);
            product.setItems(products);
            service.updateMenuItem(id, product);
        }

        model.asMap().remove("title");
        model.asMap().remove("rating");
        model.asMap().remove("baseProducts");
        return new ModelAndView(new MappingJackson2JsonView());
    }
    @DeleteMapping("/composite/{id}")
    public ModelAndView deleteComposite(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Stergerea a fost facuta cu succes!");

        service.removeMenuItem(id);

        return new ModelAndView(new MappingJackson2JsonView());
    }

    /// REPORTS
    @PostMapping("/report/1")
    public ModelAndView generateReportBetweenHours(HttpSession session, Model model, String startHour, String endHour) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Report generat cu succes!");

        val formatter = DateTimeFormatter.ofPattern("hh:mm a");
        val start = LocalTime.parse(startHour, formatter);
        val end = LocalTime.parse(endHour, formatter);

        if(end.isBefore(start)) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Ora finala trebuie sa fie inainte de ora de start!");
            return new ModelAndView(new MappingJackson2JsonView());
        }

        service.generateReportBetweenHours(start, end);

        model.asMap().remove("startHour");
        model.asMap().remove("endHour");
        return new ModelAndView(new MappingJackson2JsonView());
    }
    @PostMapping("/report/2")
    public ModelAndView generateReportProducts(HttpSession session, Model model, String minNo) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Report generat cu succes!");

        val nb = Integer.parseInt(minNo);
        service.generateReportProducts(nb);

        model.asMap().remove("minNo");
        return new ModelAndView(new MappingJackson2JsonView());
    }
    @PostMapping("/report/3")
    public ModelAndView generateReportClientSum(HttpSession session, Model model, String minOrders, String minValue) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Report generat cu succes!");

        try {
            val ordersNb = Integer.parseInt(minOrders);
            val value = Double.parseDouble(minValue);
            service.generateReportClientSum(ordersNb, value);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Valoarea trebuie sa fie un numar real!");
        }

        model.asMap().remove("minOrders");
        model.asMap().remove("minValue");
        return new ModelAndView(new MappingJackson2JsonView());
    }
    @PostMapping("/report/4")
    public ModelAndView generateReportProductsInDay(HttpSession session, Model model, String day) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Report generat cu succes!");

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        val date = LocalDate.parse(day, formatter);

        service.generateReportProductsInDay(date);

        model.asMap().remove("day");
        return new ModelAndView(new MappingJackson2JsonView());
    }
}
