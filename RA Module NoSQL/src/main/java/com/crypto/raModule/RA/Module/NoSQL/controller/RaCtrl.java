package com.crypto.raModule.RA.Module.NoSQL.controller;


import com.crypto.raModule.RA.Module.NoSQL.entities.CertRequest;
import com.crypto.raModule.RA.Module.NoSQL.entities.Requests;
import com.crypto.raModule.RA.Module.NoSQL.entities.User;
import com.crypto.raModule.RA.Module.NoSQL.helper.AppConstants;
import com.crypto.raModule.RA.Module.NoSQL.service.CertRequestService;
import com.crypto.raModule.RA.Module.NoSQL.service.RequestsService;
import com.crypto.raModule.RA.Module.NoSQL.service.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/RA")
public class RaCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private CertRequestService certRequestService;

    @Autowired
    private Gson gson;

    @PreAuthorize("hasAnyRole('RA_ADMIN','RA_OFFICER')")
    @GetMapping("/registerSubEmail")
    public String registerSubEmail(Principal principal, Model model) {
        model.addAttribute("name", principal.getName() + " reg email");
        return "registerSub";
    }


    @PostMapping(value = "/processSubEmail")
    public String processSubEmail(Model model, @RequestParam String subEmail,
                                  RedirectAttributes redirectAttributes, Principal principal) {
        System.out.println("processSubEmail :" + subEmail);

        if (requestsService.isSubscriberEmailIdPresent(subEmail.toLowerCase())) {
            System.out.println("sub email present");
            Requests request = requestsService.getRequestDtlsBySubEmail(subEmail);
            System.out.println(request.toString());
            redirectAttributes.addFlashAttribute("emailAddedBy", userService.getUserByEmail(request.getRaId()).getName());
            redirectAttributes.addFlashAttribute("request", request);
            redirectAttributes.addFlashAttribute("SubAlreadyReg", true);

        } else {
            System.out.println("sub email not present");
            redirectAttributes.addFlashAttribute("SubAlreadyReg", false);
            redirectAttributes.addFlashAttribute("emailToReg", subEmail);
        }
        return "redirect:/RA/registerSubEmail";
    }

    @PreAuthorize("hasAnyRole('RA_ADMIN','RA_OFFICER')")
    @GetMapping("/subEmail/{emailToAdd}/add")
    public String addSubEmail(Principal principal, RedirectAttributes redirectAttributes,
                              @PathVariable String emailToAdd) {

        requestsService.addNewRequest(principal.getName(), emailToAdd.toLowerCase());
        redirectAttributes.addFlashAttribute("emailAdded", true);
        redirectAttributes.addFlashAttribute("email", emailToAdd);

        return "redirect:/RA/registerSubEmail";
    }

    @PreAuthorize("hasAnyRole('RA_ADMIN','RA_OFFICER')")
    @GetMapping("/viewCertRequests")
    public String viewCerRequests(Principal principal, Model model) {

        User user = userService.getUserByEmail(principal.getName());
        String reqs = certRequestService.getCertRequestsByRaOfficeCode(user.getRaOffice().getRaOfficeCode());
        model.addAttribute("certReqs", reqs);

        return "raViewCertRequests";
    }

    @PreAuthorize("hasAnyRole('RA_ADMIN','RA_OFFICER')")
    @GetMapping("/verifyRequests")
    public String verifyRequests(Model model, Principal principal) {

        //System.out.println("to be verified: " + certRequestService.getAllCertsToBeVerified().size());

        User user = userService.getUserByEmail(principal.getName());

        if (user.isAdmin()){
            //is admin
            String raOfficeCode = userService.getUserByEmail(principal.getName()).getRaOffice().getRaOfficeCode();
            if(userService.isMoreThanOneRaAdminInRaOffice(raOfficeCode)){
                String certReqs = gson.toJson(certRequestService.getAllCertsToBeVerifiedByRaOfficeCode(user.getRaOffice().getRaOfficeCode()));
                model.addAttribute("certReqs", certReqs);
            }else {
               model.addAttribute("note","As you are the only Admin User in your RA Office you cannot Verify Requests");
            }
        }else {
            //not admin
            String certReqs = gson.toJson(certRequestService.getAllCertsToBeVerifiedByRaOfficeCode(user.getRaOffice().getRaOfficeCode()));
            model.addAttribute("certReqs", certReqs);
        }



        return "raVerifyRequests";
    }

    @PreAuthorize("hasAnyRole('RA_ADMIN','RA_OFFICER')")
    @PostMapping("/verifyChecked")
    public String verifyChecked(Model model, @RequestParam String dataToVerify, Principal principal, RedirectAttributes redirectAttributes) {

        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(dataToVerify);
        JsonArray dataArray = tradeElement.getAsJsonArray();
        System.out.println("RA email : " + principal.getName());
        int x = 0;
        for (JsonElement jsonElement : dataArray) {
            String id = String.valueOf(jsonElement.getAsJsonObject().get("sNo"));
            certRequestService.verifyCertRequest(principal.getName(), Integer.valueOf(id));
            x++;
        }
        if (x == 1) {
            redirectAttributes.addFlashAttribute("note", x + " Request Verified");
        } else {
            redirectAttributes.addFlashAttribute("note", x + " Requests Verified");
        }

        return "redirect:/RA/verifyRequests";
    }

    @PreAuthorize("hasRole('RA_ADMIN')")
    @GetMapping("/authorizeRequests")
    public String authorizeRequests(Model model, Principal principal) {

        User user = userService.getUserByEmail(principal.getName());
        String certReqs = gson.toJson(certRequestService.getAllCertsToBeAuthByRaOfficeCode(principal.getName(), user.getRaOffice().getRaOfficeCode()));
        model.addAttribute("certReqs", certReqs);

        return "raAuthRequests";
    }

    @PreAuthorize("hasRole('RA_ADMIN')")
    @PostMapping("/authorizeChecked")
    public String authChecked(Model model, @RequestParam String dataToAuth, Principal principal, RedirectAttributes redirectAttributes) {

        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(dataToAuth);
        JsonArray dataArray = tradeElement.getAsJsonArray();
        System.out.println("RA email : " + principal.getName());
        int x = 0;
        for (JsonElement jsonElement : dataArray) {
            String id = String.valueOf(jsonElement.getAsJsonObject().get("sNo"));
            certRequestService.authCertRequest(principal.getName(), Integer.valueOf(id));
            x++;
        }
        if (x == 1) {
            redirectAttributes.addFlashAttribute("note", x + " Request Authorized");
        } else {
            redirectAttributes.addFlashAttribute("note", x + " Requests Authorized");
        }

        return "redirect:/RA/authorizeRequests";
    }

    @PreAuthorize("hasAnyRole('RA_ADMIN','RA_OFFICER')")
    @PostMapping("/verifyOne/{sNo}")
    public String verifyOne(@PathVariable("sNo") int sNo, Principal principal, RedirectAttributes redirectAttributes) {

        certRequestService.verifyCertRequest(principal.getName(), sNo);
        redirectAttributes.addFlashAttribute("note", "Certificate Request " + sNo + " is Verified Successfully");

        return "redirect:/certReqDV/" + sNo;
    }

    @PreAuthorize("hasRole('RA_ADMIN')")
    @PostMapping("/authOne/{sNo}")
    public String authOne(@PathVariable("sNo") int sNo, Principal principal, RedirectAttributes redirectAttributes) {

        certRequestService.authCertRequest(principal.getName(), sNo);
        redirectAttributes.addFlashAttribute("note", "Certificate Request " + sNo + " is Authorized Successfully");

        return "redirect:/certReqDV/" + sNo;
    }

    @PreAuthorize("hasAnyRole('RA_ADMIN','RA_OFFICER')")
    @GetMapping("/certStatus/{status}")
    public String loadCertByStatus(@PathVariable("status") int status, Principal principal, Model model) {
        User user = userService.getUserByEmail(principal.getName());
        String certRequests = certRequestService.getAllCertsByRaOfficeCodeAndStatus(user.getRaOffice().getRaOfficeCode(), status);
        System.out.println(certRequests);
        model.addAttribute("certReqs", certRequests);

        switch (status) {
            case AppConstants.CERT_REQ_STATUS_PENDING_VERF:
                model.addAttribute("status", "Pending RA Verification");
                break;
            case AppConstants.CERT_REQ_STATUS_PENDING_AUTH:
                model.addAttribute("status", "Pending RA Authorization");
                break;
            case AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_VERF:
                model.addAttribute("status", "Pending CA Verification");
                break;
            case AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH:
                model.addAttribute("status", "Pending CA Authorization");
                break;
            case AppConstants.CERT_REQ_STATUS_CERT_READY_TO_GENERATE:
                model.addAttribute("status", "Processed By CA, Ready to Generate");
                break;
            case AppConstants.CERT_REQ_STATUS_CERT_READY_FOR_DOWNLOAD:
                model.addAttribute("status", "Certificate Ready to Download");
                break;
            case AppConstants.CERT_REQ_STATUS_CERT_CANCELLED:
                model.addAttribute("status", "Cancelled");
                break;
        }

        return "raShowCertReqsByStatus";

    }


   /* boolean  isMoreThanOneRaAdminInRaOffice(String email) {

        User user = userService.getUserByEmail(email);

        return userService.isMoreThanOneRaAdminInRaOffice(user.getRaOffice().getRaOfficeCode());
    }*/

}
