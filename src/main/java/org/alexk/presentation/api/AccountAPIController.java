package org.alexk.presentation.api;

import lombok.val;
import org.alexk.business.DeliveryService;
import org.alexk.business.models.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api/accounts")
public class AccountAPIController {
    DeliveryService service = DeliveryService.instance;

    @PostMapping("/login")
    public ModelAndView login(HttpSession session, Model model, User user) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Logarea a fost facuta cu succes!");

        val acc = service.checkAccount(user);
        if(acc == null) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Incorrect username or password!");
        } else {
            session.setAttribute("user", acc);

            model.addAttribute("redirect", "/app");
        }

        model.asMap().remove("user");
        return new ModelAndView(new MappingJackson2JsonView());
    }

    @PostMapping("/register")
    public ModelAndView register(HttpSession session, Model model, User user) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Inregistrarea a fost facuta cu succes!");

        if(!service.checkUsername(user.getUsername())) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Username already exists!");
        } else {
            val acc = service.createAccount(user);
            session.setAttribute("user", acc);

            model.addAttribute("redirect", "/app");
        }

        model.asMap().remove("user");
        return new ModelAndView(new MappingJackson2JsonView());
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session, Model model) {
        model.addAttribute("success", true);
        model.addAttribute("message", "Deloogarea a fost facuta cu succes!");

        session.invalidate();

        return new RedirectView("/");
    }
}
