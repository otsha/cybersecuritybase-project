package sec.project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    /*
    Event sign up with:
        - Name
        - Address
        - Password
    */
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String signupForm(Model m, @RequestParam String name, @RequestParam String address, @RequestParam String password) {
        System.out.println("SYSTEM: NEW SIGNUP > name: " + name + " time: " + System.nanoTime());
        
        // Check if password is at least 4 characters long
        // This is clearly a massive vulnerability
        if (password.length() < 4) {
            m.addAttribute("notification", "Password must be at least 4 characters long!");
            return "form";
        }
        
        Signup saved = signupRepository.save(new Signup(name, address, password));

        m.addAttribute("signup", saved);
        return "done";
    }

    /*
    User login with:
        - Name
        - Password
        - Registration number
    */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String loginForm(Model m, @RequestParam String login_name, @RequestParam String login_password, @RequestParam Long id) {
        System.out.println("SYSTEM: NEW LOGIN ATTEMPT > name: " + login_name + " time: " + System.nanoTime());

        Signup found = signupRepository.findOne(id);

        if (found != null && found.getName().equals(login_name) && found.getPassword().equals(login_password)) {

            m.addAttribute("signup", found);

            return "registration";
        } else {
            return "form";
        }
    }

    // List all event sign-ups
    @RequestMapping(value = "/signups", method = RequestMethod.GET)
    public String signupList(Model m) {
        List<Signup> allSignups = signupRepository.findAll();
        m.addAttribute("signups", allSignups);

        return "signups";
    }

    /* 
    VULNERABILITY: BROKEN ACCESS CONTROL
    The following method has multiple issues:
    1. It is not using the proper HTTP verb (should be using DELETE)
    2. It does not check whether or not the request is authorized, in other words,
    whether the deletion request comes from within the application and if
    the requester is allowed to remove this registration.
    
    This allows anyone to remove registrations, as long as they know their IDs.
     */
    @RequestMapping(value = "/signups/remove/{id}")
    public String removeSignUp(@PathVariable Long id) {
        signupRepository.delete(id);

        return "redirect:/signups";
    }

}
