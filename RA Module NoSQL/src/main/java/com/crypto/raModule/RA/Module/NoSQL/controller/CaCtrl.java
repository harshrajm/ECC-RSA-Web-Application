package com.crypto.raModule.RA.Module.NoSQL.controller;

import com.crypto.raModule.RA.Module.NoSQL.entities.User;
import com.crypto.raModule.RA.Module.NoSQL.helper.AppConstants;
import com.crypto.raModule.RA.Module.NoSQL.service.CertRequestService;
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

@Controller
@RequestMapping("/CA")
public class CaCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private CertRequestService certRequestService;

    @Autowired
    private Gson gson;

    @PreAuthorize("hasAnyRole('CA_OFFICER')")
    @GetMapping("/verifyRequests")
    public String verifyRequests(Model model, Principal principal) {

        String certReqs = gson.toJson(certRequestService.getAllCertsInPendingCaVerification());
        model.addAttribute("certReqs", certReqs);

        return "caVerifyRequests";
    }

    @PreAuthorize("hasAnyRole('CA_OFFICER')")
    @RequestMapping("/verifyChecked")
    public String verifyChecked(Model model, @RequestParam String dataToVerify, Principal principal, RedirectAttributes redirectAttributes) {

        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(dataToVerify);
        JsonArray dataArray = tradeElement.getAsJsonArray();
        System.out.println("CA email : " + principal.getName());
        int x = 0;
        for (JsonElement jsonElement : dataArray) {
            String id = String.valueOf(jsonElement.getAsJsonObject().get("sNo"));
            certRequestService.verifyCertRequestByCa(principal.getName(), Integer.valueOf(id));
            x++;
        }
        if (x == 1) {
            redirectAttributes.addFlashAttribute("note", x + " Request Verified");
        } else {
            redirectAttributes.addFlashAttribute("note", x + " Requests Verified");
        }

        return "redirect:/CA/verifyRequests";
    }



    @PreAuthorize("hasAnyRole('CA_ADMIN')")
    @GetMapping("/authorizeRequests")
    public String authRequests(Model model, Principal principal) {

        String certReqs = gson.toJson(certRequestService.getAllCertsInPendingCaAuthorization());
        model.addAttribute("certReqs", certReqs);

        return "caAuthRequests";
    }


    @PreAuthorize("hasAnyRole('CA_ADMIN')")
    @RequestMapping("/authorizeChecked")
    public String authChecked(Model model, @RequestParam String dataToAuth, Principal principal, RedirectAttributes redirectAttributes) {

        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(dataToAuth);
        JsonArray dataArray = tradeElement.getAsJsonArray();
        System.out.println("CA email : " + principal.getName());
        int x = 0;
        for (JsonElement jsonElement : dataArray) {
            String id = String.valueOf(jsonElement.getAsJsonObject().get("sNo"));
            certRequestService.authCertRequestByCa(principal.getName(), Integer.valueOf(id));
            x++;
        }
        if (x == 1) {
            redirectAttributes.addFlashAttribute("note", x + " Request Authorized");
        } else {
            redirectAttributes.addFlashAttribute("note", x + " Requests Authorized");
        }

        return "redirect:/CA/authorizeRequests";
    }


    @PreAuthorize("hasAnyRole('CA_ADMIN','CA_OFFICER')")
    @GetMapping("/viewCertRequests")
    public String viewCerRequests(Principal principal, Model model) {

        //User user = userService.getUserByEmail(principal.getName());
        String reqs = certRequestService.getAllCertReqs();
        model.addAttribute("certReqs", reqs);

        return "caViewCertRequests";
    }


    @PreAuthorize("hasAnyRole('CA_ADMIN','CA_OFFICER')")
    @GetMapping("/certStatus/{raOfficeCode}/{status}")
    public String loadCertByStatus(@PathVariable("raOfficeCode") String raOfficeCode,@PathVariable("status") int status, Principal principal, Model model) {
        //User user = userService.getUserByEmail(principal.getName());
        String certRequests = certRequestService.getAllCertsByRaOfficeCodeAndStatus(raOfficeCode, status);
        //String certRequests = certRequestService.getAllCertsByStatus(status);
        System.out.println(certRequests);
        model.addAttribute("certReqs", certRequests);

        switch (status) {
            case AppConstants.CERT_REQ_STATUS_PENDING_VERF:
                model.addAttribute("status", "Pending RA Verification in "+raOfficeCode);
                break;
            case AppConstants.CERT_REQ_STATUS_PENDING_AUTH:
                model.addAttribute("status", "Pending RA Authorization in "+raOfficeCode);
                break;
            case AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_VERF:
                model.addAttribute("status", "Pending CA Verification in "+raOfficeCode);
                break;
            case AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH:
                model.addAttribute("status", "Pending CA Authorization in "+raOfficeCode);
                break;
            case AppConstants.CERT_REQ_STATUS_CERT_READY_TO_GENERATE:
                model.addAttribute("status", "Processed By CA, Ready to Generate in "+raOfficeCode);
                break;
            case AppConstants.CERT_REQ_STATUS_CERT_READY_FOR_DOWNLOAD:
                model.addAttribute("status", "Certificate Ready to Download in "+raOfficeCode);
                break;
            case AppConstants.CERT_REQ_STATUS_CERT_CANCELLED:
                model.addAttribute("status", "Cancelled in "+raOfficeCode);
                break;
        }

        return "caShowCertReqsByStatus";

    }


    @PreAuthorize("hasAnyRole('CA_ADMIN','CA_OFFICER')")
    @GetMapping("/viewCertRequests/{raOfficeCode}")
    public String viewCerRequestsByRaOffice(@PathVariable("raOfficeCode") String raOfficeCode,Principal principal, Model model) {

        String certRequests = certRequestService.getAllCertsByRaOfficeCodeForCa(raOfficeCode);
        //String certRequests = certRequestService.getAllCertsByStatus(status);
        System.out.println(certRequests);
        model.addAttribute("certReqs", certRequests);
        model.addAttribute("status", "All Requests in "+raOfficeCode);
        return "caShowCertReqsByStatus";
    }

    @PreAuthorize("hasAnyRole('CA_OFFICER')")
    @PostMapping("/verifyOne/{sNo}")
    public String verifyOne(@PathVariable("sNo") int sNo, Principal principal, RedirectAttributes redirectAttributes) {

        certRequestService.caVerifyCertRequest(principal.getName(), sNo);
        redirectAttributes.addFlashAttribute("note", "Certificate Request " + sNo + " is Verified by CA Successfully");

        return "redirect:/caCertReqDV/" + sNo;
    }

    @PreAuthorize("hasRole('CA_ADMIN')")
    @PostMapping("/authOne/{sNo}")
    public String authOne(@PathVariable("sNo") int sNo, Principal principal, RedirectAttributes redirectAttributes) {

        certRequestService.caAuthCertRequest(principal.getName(), sNo);
        redirectAttributes.addFlashAttribute("note", "Certificate Request " + sNo + " is Authorized by CA Successfully");

        return "redirect:/caCertReqDV/" + sNo;
    }

}
