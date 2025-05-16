package com.dafnis.AppSpringMySQL.services;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.dafnis.AppSpringMySQL.DTOModels.PaymentDTO;
import com.dafnis.AppSpringMySQL.models.Customer;
import com.dafnis.AppSpringMySQL.models.Payment;
import com.dafnis.AppSpringMySQL.models.Rental;
import com.dafnis.AppSpringMySQL.models.Staff;
import com.dafnis.AppSpringMySQL.repo.CustomerRepository;
import com.dafnis.AppSpringMySQL.repo.PaymentRepository;
import com.dafnis.AppSpringMySQL.repo.RentalRepository;
import com.dafnis.AppSpringMySQL.repo.StaffRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;
    private CustomerRepository customerRepository;
    private StaffRepository staffRepository;
    private RentalRepository rentalRepository;
    private EntityManager entityManager;
    public PaymentService(PaymentRepository paymentRepository, CustomerRepository customerRepository, 
                            StaffRepository staffRepository, RentalRepository rentalRepository, EntityManager entityManager){
        this.paymentRepository = paymentRepository;
        this.rentalRepository = rentalRepository;
        this.entityManager = entityManager;
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    public Page<Payment> getAll(int numPagina, int tamañoPagina){
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return paymentRepository.findAll(pageable);
    }

    public Payment getPaymentById(Integer id)throws NoSuchElementException{
        Optional<Payment> opt = paymentRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new NoSuchElementException("Payment no encontrado.");
    }

    public Page<Payment> getPaymentsByCustomerId(Integer id, int numPagina, int tamañoPagina)throws NoSuchElementException{
        if(!customerRepository.existsById(id)){
            throw new NoSuchElementException("Customer no encontrado.");
        }
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return paymentRepository.findByCustomerId(id, pageable);
    }

    public Page<Payment> getPaymentsByStaffId(Integer id, int numPagina, int tamañoPagina)throws NoSuchElementException{
        if(!staffRepository.existsById(id)){
            throw new NoSuchElementException("Staff no encontrado.");
        }
        Pageable pageable = PageRequest.of(numPagina - 1, tamañoPagina);
        return paymentRepository.findByStaffId(id, pageable);
    }

    @Transactional
    public Payment savePayment(Payment payment)throws InternalException, NoSuchElementException{
        Optional<Customer> optCustomer = customerRepository.findById(payment.getCustomerIdIntroducido());
        payment.setCustomer(optCustomer.orElseThrow(() -> new NoSuchElementException("Customer no encontrado.")));
        Optional<Staff> optStaff = staffRepository.findById(payment.getStaffIdIntroducido());
        payment.setStaff(optStaff.orElseThrow(() -> new NoSuchElementException("Staff no encontrado.")));
        Optional<Rental> optRental = rentalRepository.findById(payment.getRentalIdIntroducido());
        payment.setRental(optRental.orElseThrow(() -> new NoSuchElementException("Rental no encontrado.")));
        try{
            Payment paymentGuardado = paymentRepository.save(payment);
            entityManager.flush();
            entityManager.clear();
            Optional<Payment> opt = paymentRepository.findById(paymentGuardado.getId());
            if(opt.isPresent()){
                return opt.get();
            }
            throw new InternalException("Error al recuperar después de guardar.");
        }catch(PersistenceException e){
            throw new InternalException("Error de persistencia.");
        }
    }

    @Transactional
    public Payment updatePayment(Integer id, PaymentDTO paymentDTO)throws NoSuchElementException, InternalException{
        Optional<Payment> optPayment = paymentRepository.findById(id);
        if(!optPayment.isPresent()){
            throw new NoSuchElementException("Payment no encontrado.");
        }
        Payment payment = optPayment.get();
        if(paymentDTO.getAmount() != null){
            payment.setAmount(paymentDTO.getAmount());
        }
        if(paymentDTO.getCustomer_id() != null){
            Optional<Customer> optCustomer = customerRepository.findById(paymentDTO.getCustomer_id());
            payment.setCustomer(optCustomer.orElseThrow(() -> new NoSuchElementException("Customer no encontrado.")));
        }
        if(paymentDTO.getRental_id() != null){
            Optional<Rental> optRental = rentalRepository.findById(paymentDTO.getRental_id());
            payment.setRental(optRental.orElseThrow(() -> new NoSuchElementException("Rental no encontrado.")));
        }
        if(paymentDTO.getStaff_id() != null){
            Optional<Staff> optStaff = staffRepository.findById(paymentDTO.getStaff_id());
            payment.setStaff(optStaff.orElseThrow(() -> new NoSuchElementException("Staff no encontrado.")));
        }
        paymentRepository.save(payment);
        entityManager.flush();
        entityManager.clear();
        return paymentRepository.findById(id).orElseThrow(() -> new InternalException("Error al recuperar después de guardar."));
    }

    @Transactional
    public void deletePayment(Integer id)throws NoSuchElementException{
        if(!paymentRepository.existsById(id)){
            throw new NoSuchElementException("Payment no encontrado.");
        }
        paymentRepository.deleteById(id);
    }

    
}
