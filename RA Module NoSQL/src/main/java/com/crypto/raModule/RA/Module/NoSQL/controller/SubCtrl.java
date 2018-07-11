package com.crypto.raModule.RA.Module.NoSQL.controller;

import com.crypto.raModule.RA.Module.NoSQL.entities.CertRequest;
import com.crypto.raModule.RA.Module.NoSQL.entities.Csr;
import com.crypto.raModule.RA.Module.NoSQL.entities.RaOffice;
import com.crypto.raModule.RA.Module.NoSQL.service.CertRequestService;
import com.crypto.raModule.RA.Module.NoSQL.service.RequestsService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/sub")
@Controller
public class SubCtrl {

    @Autowired
    private CertRequestService certRequestService;

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private Gson gson;


    @GetMapping("/enterEmail")
    public String subEnterEmail(Model model) {

        model.addAttribute("test", "test Data");

        return "subEnterEmail";
    }

    @PostMapping("/subEmail")
    public String subEmail(Model model, @RequestParam String subEmail, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        System.out.println("email entered : " + subEmail);
        if (requestsService.isSubscriberEmailIdPresent(subEmail.toLowerCase())) {

            System.out.println("Email ID Present ask for OTP");
            model.addAttribute("subEmail", subEmail);
            httpSession.setAttribute("subEmail", subEmail);
            return "subEnterOTP";
        } else {
            System.out.println("Email ID Not present show email page");
            redirectAttributes.addFlashAttribute("note", "Email Address " + subEmail + " is not registered with us, contact your RA Officer");
            return "redirect:/sub/enterEmail";
        }


    }

    @PostMapping("/subOTP")
    public String subOTP(Model model, @RequestParam String subOTP, HttpSession httpSession) {

        System.out.println("OTP entered:" + subOTP + " By :" + httpSession.getAttribute("subEmail"));

        if (requestsService.isOtpMatching(subOTP, (String) httpSession.getAttribute("subEmail"))) {

            System.out.println("OTP Valid");

            return "subAuthSuccess";
        } else {


            System.out.println("OTP Not Valid");
            model.addAttribute("note", "OTP entered is invalid!");
            model.addAttribute("subEmail", httpSession.getAttribute("subEmail"));
            return "subEnterOTP";

        }

    }

    @PostMapping("/goSubHome")
    public String goSubHome() {
        return "subAuthSuccess";
    }

    @PostMapping("/requestNewCert")
    public String requestNewCert(Model model, HttpSession httpSession,RedirectAttributes redirectAttributes) {

        if(null  == httpSession.getAttribute("subEmail")){
            redirectAttributes.addFlashAttribute("note", "You performed an action which is not allowed. Please login again");
            return "redirect:/sub/enterEmail";
        }
        System.out.println("New Cert Request clicked by " + httpSession.getAttribute("subEmail"));
        model.addAttribute("subEmail", httpSession.getAttribute("subEmail"));
        return "certRequestForm";
    }

    /*@GetMapping("/requestNewCert")
    public String requestNewCertGet(Model model, HttpSession httpSession,RedirectAttributes redirectAttributes){

        try {
            model.addAttribute("subEmail",httpSession.getAttribute("subEmail"));
            return "certRequestForm";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("note","Login to proceed");
            return "redirect:/sub/enterEmail";
        }

    }*/

    @GetMapping({"/subEmail", "/subOTP", "/goSubHome", "/requestNewCert", "trackSubCerts"})
    public String actionNotAllowed(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("note", "You performed an action which is not allowed. Please login again");
        return "redirect:/sub/enterEmail";
    }

    @PostMapping("/submitCertRequest")
    public String submitCertRequest(Model model, @RequestParam("dn") String distinguishedName,
                                    @RequestParam("o") String organization, @RequestParam("ou") String organizationUnit,
                                    @RequestParam("pn") String panNumber, @RequestParam("c") String city,
                                    @RequestParam("s") String state, @RequestParam("class") int classOfCert,
                                    @RequestParam("validity") int validityOfCert, @RequestParam String comnts,
                                    @RequestParam("type") int type, HttpSession httpSession) {
        Csr csr = new Csr();
        csr.setDistinguishedName(distinguishedName);
        csr.setOrganization(organization);
        csr.setOrganizationalUnit(organizationUnit);
        csr.setCity(city);
        csr.setState(state);
        csr.setCountry("IN");
        csr.setEmail((String) httpSession.getAttribute("subEmail"));


        boolean isEcc = false;
        if (type == 1) {
            isEcc = true;

        }
        int reqNo = certRequestService.newCertRequest(csr, panNumber, classOfCert, validityOfCert, comnts, (String) httpSession.getAttribute("subEmail"), isEcc);

        model.addAttribute("note", "Certificate Request submitted successfully with Request Number " + reqNo);

        return "subAuthSuccess";
    }


    @PostMapping("/trackSubCerts")
    public String trackSubCerts(Model model, HttpSession httpSession,RedirectAttributes redirectAttributes) {

        if(null  == httpSession.getAttribute("subEmail")){
            redirectAttributes.addFlashAttribute("note", "You performed an action which is not allowed. Please login again");
            return "redirect:/sub/enterEmail";
        }

        model.addAttribute("test", "testing da " + httpSession.getAttribute("subEmail"));

        List<CertRequest> certRequests = certRequestService.getAllCertRequestsBySubEmail((String) httpSession.getAttribute("subEmail"));

        if (certRequests.isEmpty()) {
            System.out.println("List is empty!");
            model.addAttribute("note", "No Requests to show");
        } else {
            System.out.println("List is not empty! " + certRequests.size());
            String json = gson.toJson(certRequests);
            System.out.println(json);
            model.addAttribute("subCertReqs", json);
        }
        return "subCertRequests";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession, Model model) {
        httpSession.invalidate();
        model.addAttribute("note", "You have been logged out");
        return "subEnterEmail";
    }
}
