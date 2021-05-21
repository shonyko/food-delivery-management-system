package org.alexk.presentation.api;

import lombok.val;
import org.alexk.business.DeliveryService;
import org.alexk.business.models.viewModels.MenuItemViewModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@RestController
@RequestMapping(value = "/api/employee")
public class EmployeeAPIController implements PropertyChangeListener {
    DeliveryService service = DeliveryService.instance;
    final SimpMessagingTemplate simpMessagingTemplate;

    public EmployeeAPIController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;

        service.addPropertyChangeListener(this);
    }

    @GetMapping("/orders")
    public ModelAndView getOrders(HttpSession session, Model model) {
        model.addAttribute("success", true);

        val orders = service.getOrders();
        model.addAttribute("data", orders);

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/orders/{id}")
    public ModelAndView getOrderDetails(HttpSession session, Model model, @PathVariable int id) {
        model.addAttribute("success", true);

        try {
            val items = service.getOrderItems(id).stream().map(MenuItemViewModel::new);
            model.addAttribute("data", items);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Comanda nu exista!");
        }

        return new ModelAndView(new MappingJackson2JsonView());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equalsIgnoreCase("orders")) {
            simpMessagingTemplate.convertAndSend("/employees", "{\"success\":\"true\", \"message\":\"Un nou order a fost adaugat!\"}");
        }
    }
}
