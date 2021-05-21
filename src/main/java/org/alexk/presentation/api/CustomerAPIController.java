package org.alexk.presentation.api;

import lombok.val;
import org.alexk.business.DeliveryService;
import org.alexk.business.IDeliveryServiceProcessing;
import org.alexk.business.models.CompositeProduct;
import org.alexk.business.models.OrderItem;
import org.alexk.business.models.User;
import org.alexk.business.models.viewModels.MenuItemViewModel;
import org.alexk.utils.FilterConfigBuilder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/customer")
public class CustomerAPIController {
    IDeliveryServiceProcessing service = DeliveryService.instance;

    @GetMapping("/scout")
    public ModelAndView getData(HttpSession session, Model model, FilterConfigBuilder filterBuilder) {
        val filter = filterBuilder.getConfig();
        val menuItems = service.filterMenuItems(filter);

        model.addAttribute("noItems", menuItems.size());

        model.asMap().remove("filterBuilder");
        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/shop/{from}/{to}")
    public ModelAndView getAll(HttpSession session, Model model, @PathVariable int from, @PathVariable int to,
                               FilterConfigBuilder filterBuilder) {
        model.addAttribute("success", true);

        System.out.println(String.format("%d to %d", from, to));

        val filter = filterBuilder.getConfig();
        val filterMenuItems = service.filterMenuItems(filter).subList(from, to);
        val items = filterMenuItems.stream()
                .map(item -> new MenuItemViewModel(item.getId(), item))
                .collect(Collectors.toList());

        model.addAttribute("data", items);

        model.asMap().remove("filterBuilder");
        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/cart")
    public ModelAndView getCart(HttpSession session, Model model) {
        model.addAttribute("success", true);

        var map = (Map<Integer, OrderItem>) session.getAttribute("cart");
        if(map == null) {
            map = new HashMap<>();
        }

        model.addAttribute("data", map.values());

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @PostMapping("/cart/{id}")
    public ModelAndView addToCart(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Adaugarea a fost facuta cu succes!");

        var map = (Map<Integer, MenuItemViewModel>) session.getAttribute("cart");
        if(map == null) {
            map = new HashMap<>();
        }

        if(map.containsKey(id)) {
            val el = map.get(id);
            el.setQuantity(el.getQuantity() + 1);
            el.updatePrice();
        } else {
            val item = new MenuItemViewModel(id, service.getMenuItem(id));
            map.put(id, item);
        }

        session.setAttribute("cart", map);

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @DeleteMapping("/cart/{id}")
    public ModelAndView removeFromCart(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Stergerea a fost facuta cu succes!");

        var map = (Map<Integer, MenuItemViewModel>) session.getAttribute("cart");
        if(map == null) {
            map = new HashMap<>();
        }

        if(map.containsKey(id)) {
            map.remove(id);
        }

        session.setAttribute("cart", map);

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @PostMapping("/order")
    public ModelAndView order(HttpSession session, Model model) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Comanda a fost plasata!");

        var map = (Map<Integer, MenuItemViewModel>) session.getAttribute("cart");
        if(map == null) {
            map = new HashMap<>();
        }

        if(map.values().size() <= 0) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Comanda trebuie sa contina minim un produs!");
        } else {
            val user = (User) session.getAttribute("user");
            if(user == null) {
                return new ModelAndView("redirect:/");
            }

            service.createOrder(user, map.values().stream().collect(Collectors.toList()));
            session.removeAttribute("cart");
        }

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/product/{id}")
    public ModelAndView getProductDetails(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);

        try {
            val item = service.getMenuItem(id);
            if(item instanceof CompositeProduct) {
                model.addAttribute("data", ((CompositeProduct)item).getItems());
            } else {
                model.addAttribute("data", new ArrayList<>());
            }
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Produsul nu exista!");
        }

        return new ModelAndView(new MappingJackson2JsonView());
    }
}
