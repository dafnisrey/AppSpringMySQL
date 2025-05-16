package com.dafnis.AppSpringMySQL.controllers;

import java.util.NoSuchElementException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dafnis.AppSpringMySQL.DTOModels.PaymentDTO;
import com.dafnis.AppSpringMySQL.models.Payment;
import com.dafnis.AppSpringMySQL.services.PaymentService;
import jakarta.validation.Valid;

@RestController
public class PaymentController {

    private PaymentService paymentService;
    private PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @GetMapping("/payments")
    public ResponseEntity<Page<Payment>> getAll(@RequestParam int numPagina, @RequestParam int tamañoPagina){
        return ResponseEntity.ok(paymentService.getAll(numPagina, tamañoPagina));
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(paymentService.getPaymentById(id));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/customer/{id}/payments")
    public ResponseEntity<?> getPaymentsByCustomerId(@PathVariable Integer id,
                                                            @RequestParam int numPagina,
                                                            @RequestParam int tamañoPagina){
        try{
            return ResponseEntity.ok(paymentService.getPaymentsByCustomerId(id, numPagina, tamañoPagina));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/staff/{id}/payments")
    public ResponseEntity<?> getPaymentsByStaffId(@PathVariable Integer id,
                                                            @RequestParam int numPagina,
                                                            @RequestParam int tamañoPagina){
        try{
            return ResponseEntity.ok(paymentService.getPaymentsByStaffId(id, numPagina, tamañoPagina));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> savePayment(@RequestBody @Valid Payment payment){
        if(payment.getCustomerIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un customer_id.");
        }
        if(payment.getRentalIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un rental_id.");
        }
        if(payment.getStaffIdIntroducido() == null){
            return ResponseEntity.badRequest().body("Debes introducir un staff_id.");
        }
        try{
            return ResponseEntity.ok(paymentService.savePayment(payment));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/payment/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable Integer id, @RequestBody PaymentDTO paymentDTO){
        try{
            return ResponseEntity.ok(paymentService.updatePayment(id, paymentDTO));
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(InternalException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/payment/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Integer id){
        try{
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}
