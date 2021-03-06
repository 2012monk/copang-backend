package com.alconn.copang.client;

import com.alconn.copang.auth.AccessTokenContainer;
import com.alconn.copang.auth.LoginToken;
import com.alconn.copang.client.UserForm.Response;
import com.alconn.copang.client.UserForm.SellerResponse;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.security.provider.JwtTokenProvider;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.seller.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientMapper mapper;

    private final ClientRepo repo;

    private final JwtTokenProvider provider;

    private final BCryptPasswordEncoder passwordEncoder;

    private final SellerRepository sellerRepository;

    public AccessTokenContainer login(LoginToken token) throws LoginFailedException, InvalidTokenException {
        Client client = repo.findClientByUsername(token.getUsername()).orElseThrow(LoginFailedException::new);
        if (client.getPassword().equals(token.getPassword())) {
            String accessToken = provider.createAccessToken(client).orElseThrow(InvalidTokenException::new);
            return getAccessTokenContainer(client);
        } else {
            throw new LoginFailedException("password unmatched");
        }
    }

    public Client signupClient(Client client) {
        // TODO // validate login *//

        return repo.save(client);

    }

    @Transactional
    public Client register(UserForm userForm) {
        userForm.setRole(Role.CLIENT);
        Client client = mapper.toEntity(userForm);

        return repo.save(client);
    }

    public Client getClient(Long id) throws NoSuchUserException {
        return repo.findById(id).orElseThrow(NoSuchUserException::new);
    }

    private AccessTokenContainer getAccessTokenContainer(Client client) {

        String username = client.getUsername();
        Role role = client.getRole();
        String token = provider.createAccessToken(client).orElse("");
        return AccessTokenContainer.builder()
                .access_token(token)
                .username(username)
                .role(role)
                .build();
    }

    public AccessTokenContainer getAccessTokenFromRefreshToken(String token) throws InvalidTokenException, NoSuchUserException {
        Long id = provider.getUserIdFromRefreshToken(token).orElseThrow(InvalidTokenException::new);

        Client client = repo.findById(id).orElseThrow(NoSuchUserException::new);

        return getAccessTokenContainer(client);
    }

    public List<Client> getAllClients() {
        return repo.findAll();
    }

//    @Transactional
//    public Client updateClient(Client client) throws NoSuchUserException {
//        Client subject = repo.findClientByUsername(client.getUsername()).orElseThrow(NoSuchUserException::new);
//    }

    @Transactional
    public Client updateClient(UserForm form, Long id) throws NoSuchUserException, ValidationException {
        if (id == null || id == 0){
            throw new ValidationException("??????????????? ?????????????????? ?????????????????????");
        }
        Client subject = repo.findById(id).orElseThrow(ValidationException::new);
        subject.updateInfo(form.getPhone(), form.getRealName());
        return repo.save(subject);
    }

    @Transactional
    public Client signupClient(UserForm form) throws SQLIntegrityConstraintViolationException {

        checkOverlapUser(form);
        form.setRole(Role.CLIENT);
        Client client = mapper.toEntity(form);
//        Client client = mapper.toEntityClass(form, Client.class);
        return repo.save(client);
    }

    /**
     *
     * @param form ?????????
     * @throws SQLIntegrityConstraintViolationException ????????? ???????????? ????????? Exception
     */
    private void checkOverlapUser(UserForm form) throws SQLIntegrityConstraintViolationException {
        if (checkUsername(form.getUsername())){
            throw new SQLIntegrityConstraintViolationException("???????????? ?????????????????????");
        }
    }

    /**
     *
     * @param username ?????? ?????????
     * @return ????????? ????????? ????????? True
     */
    private boolean checkUsername(String username) {
        return repo.findClientByUsername(username).isPresent();
    }

    @Transactional
    public boolean deleteClient(Long id) throws NoSuchUserException {
        try{
            repo.deleteById(id);
            return true;
        }catch (Exception e){
            log.info("invalid data",e);
            throw new NoSuchUserException("???????????? ????????? ?????????????????????");
        }
    }

    @Transactional
    public UserForm.Response registerSeller(UserForm form) throws SQLIntegrityConstraintViolationException {
        checkOverlapUser(form);
        form.setRole(Role.SELLER);
        Seller client = mapper.toSeller(form);

        sellerRepository.save(client);

        return mapper.toResponse(client);

    }

    public SellerResponse getSeller(Long sellerId) throws NoSuchUserException {

        Seller seller = sellerRepository.findById(sellerId).orElseThrow(NoSuchUserException::new);

        return mapper.toResponse(seller);
    }
}
