package com.crypto.raModule.RA.Module.NoSQL.controller;

import com.crypto.raModule.RA.Module.NoSQL.entities.CertRequest;
import com.crypto.raModule.RA.Module.NoSQL.entities.User;
import com.crypto.raModule.RA.Module.NoSQL.helper.AppConstants;
import com.crypto.raModule.RA.Module.NoSQL.repos.CertRequestRepository;
import com.crypto.raModule.RA.Module.NoSQL.service.CertRequestService;
import com.crypto.raModule.RA.Module.NoSQL.service.RaOfficeDtlService;
import com.crypto.raModule.RA.Module.NoSQL.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@Controller
public class HomeCtrl {

    @Autowired
    private CertRequestService certRequestService;

    @Autowired
    private Gson gson;

    @Autowired
    private UserService userService;

    @Autowired
    RaOfficeDtlService raOfficeDtlService;

    @RequestMapping("/home")
    public String homePage(Model model, Principal principal) {
        model.addAttribute("name", principal.getName());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isRaOfficer = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RA_OFFICER"));
        boolean isRaAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RA_ADMIN"));
        boolean isCaOfficer = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CA_OFFICER"));
        boolean isCaAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CA_ADMIN"));

        System.out.println("is ra officer " + isRaOfficer);
        System.out.println("is ra admin " + isRaAdmin);
        System.out.println("is ca officer " + isCaOfficer);
        System.out.println("is ca admin " + isCaAdmin);

        User user = userService.getUserByEmail(principal.getName());

        if (isRaOfficer || isRaAdmin) {

            List<CertRequest> certRequests = certRequestService.getAllCertsByRaOfficeCode(user.getRaOffice().getRaOfficeCode());

            int totalReq = 0;
            int penRaVer = 0;
            int penRaAuth = 0;
            int penCaVer = 0;
            int penCaAuth = 0;
            int readyToGen = 0;
            int readyToDnld = 0;
            int cncld = 0;

            totalReq = certRequests.size();

            for (CertRequest certRequest : certRequests) {

                switch (certRequest.getStatus()) {
                    case AppConstants.CERT_REQ_STATUS_PENDING_VERF:
                        penRaVer++;
                        break;
                    case AppConstants.CERT_REQ_STATUS_PENDING_AUTH:
                        penRaAuth++;
                        break;
                    case AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_VERF:
                        penCaVer++;
                        break;
                    case AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH:
                        penCaAuth++;
                        break;
                    case AppConstants.CERT_REQ_STATUS_CERT_READY_TO_GENERATE:
                        readyToGen++;
                        break;
                    case AppConstants.CERT_REQ_STATUS_CERT_READY_FOR_DOWNLOAD:
                        readyToDnld++;
                        break;
                    case AppConstants.CERT_REQ_STATUS_CERT_CANCELLED:
                        cncld++;
                        break;
                }


            }

            model.addAttribute("total",totalReq);
            model.addAttribute("penRaVer",penRaVer);
            model.addAttribute("penRaAuth",penRaAuth);
            model.addAttribute("penCaVer",penCaVer);
            model.addAttribute("penCaAuth",penCaAuth);
            model.addAttribute("rdyToGen",readyToGen);
            model.addAttribute("rdyToDnld",readyToDnld);
            model.addAttribute("cancelled",cncld);

            if (user.isAdmin()) {
                model.addAttribute("role", "RA Adminstrator");
            } else {
                model.addAttribute("role", "RA Officer");
            }
            model.addAttribute("raOffice", user.getRaOffice());
            model.addAttribute("name", user.getName());

            if(penRaAuth > 0){
                model.addAttribute("toDoAuth",true);
            }
            if (penRaVer > 0){
                model.addAttribute("toDoVer",true);
            }

            if(penRaAuth > 0 || penRaVer > 0){
                model.addAttribute("showTodo",true);
            }

            if(penRaVer == 0 && isRaOfficer){
                model.addAttribute("showTodo",false);

            }

            // do not show verify in todo if only ra Admin
            if (isRaAdmin){
                String raOfficeCode = userService.getUserByEmail(principal.getName()).getRaOffice().getRaOfficeCode();
                if(userService.isMoreThanOneRaAdminInRaOffice(raOfficeCode)){
                    model.addAttribute("toDoVer",true);
                }else {
                    model.addAttribute("toDoVer",false);
                }
            }

            return "raHome";
        }

        if (isCaAdmin || isCaOfficer) {

            if (user.isAdmin()) {
                model.addAttribute("role", "CA Adminstrator");
            } else {
                model.addAttribute("role", "CA Officer");
            }
            model.addAttribute("caOffice", user.getRaOffice());
            model.addAttribute("name", user.getName());

            int noOfReqInPendCaVerf = certRequestService.getNoOfReqByStatus(AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_VERF);
            int noOfReqInPendCaAuth = certRequestService.getNoOfReqByStatus(AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH);

            model.addAttribute("noOfReqInPendCaVerf",noOfReqInPendCaVerf);
            model.addAttribute("noOfReqInPendCaAuth",noOfReqInPendCaAuth);
            model.addAttribute("raOfficeDtls",raOfficeDtlService.loadAllRaOfficeCodes());
            model.addAttribute("raOfficeDtls1",gson.toJson(raOfficeDtlService.loadAllRaOfficeCodes()));

            return "caHome";
        }

        return "";

    }

    @PreAuthorize("hasAnyRole('RA_ADMIN','RA_OFFICER')")
    @RequestMapping("/certReqDV/{sNo}")
    public String certReqDV(@PathVariable("sNo") Integer sNo, Model model, Principal principal) {
        System.out.println("in certReqDV");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isRaOfficer = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RA_OFFICER"));
        boolean isRaAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RA_ADMIN"));

        CertRequest certRequest = certRequestService.getCertReqBySno(sNo);
        if (isRaAdmin) {
            certRequest.setRaAdmin(true);
            certRequest.setAllowVerif(true);
            certRequest.setAllowAuth(true);
        }
        if (isRaOfficer) {
            certRequest.setRaOfficer(true);
            certRequest.setAllowVerif(true);
        }

        //if admin verified then cannot Auth
        if (certRequest.getStatus() == AppConstants.CERT_REQ_STATUS_PENDING_AUTH && isRaAdmin) {
            if (certRequest.getVerifiedByRaEmail().equalsIgnoreCase(principal.getName())) {
                certRequest.setAllowAuth(false);
            }
        }

        //if only One Admin Ra in Ra Office Then Cannot Verify
        if (isRaAdmin){
            String raOfficeCode = userService.getUserByEmail(principal.getName()).getRaOffice().getRaOfficeCode();
            if(userService.isMoreThanOneRaAdminInRaOffice(raOfficeCode)){
                certRequest.setAllowVerif(true);
            }else {
                certRequest.setAllowVerif(false);
            }
        }





        System.out.println(certRequest);
        System.out.println("RA officer " + isRaOfficer + " : RA admin " + isRaAdmin);
        model.addAttribute("reqDtls", gson.toJson(certRequest));

        return "certReqDV";
    }

    @PreAuthorize("hasAnyRole('CA_ADMIN','CA_OFFICER')")
    @RequestMapping("/caCertReqDV/{sNo}")
    public String caCertReqDV(@PathVariable("sNo") Integer sNo, Model model) {

        System.out.println("in certReqDV");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isCaOfficer = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CA_OFFICER"));
        boolean isCaAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CA_ADMIN"));
        CertRequest certRequest = certRequestService.getCertReqBySno(sNo);

        if(isCaOfficer){
            certRequest.setCaOfficer(true);
        }

        if(isCaAdmin){
            certRequest.setCaAdmin(true);
        }

        model.addAttribute("reqDtls", gson.toJson(certRequest));
        return "caCertReqDV";
    }



}
