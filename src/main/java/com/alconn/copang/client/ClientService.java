package com.alconn.copang.client;

import com.alconn.copang.common.AccessTokenContainer;
import com.alconn.copang.auth.LoginToken;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.security.privider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ModelMapper mapper;

    private final ClientRepo repo;

    private final JwtTokenProvider provider;

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
    public Client updateClient(UserForm form) throws NoSuchUserException, ValidationException {
        if (form.getId() == 0){
            throw new ValidationException("업데이트시 유저아이디가 존재해야합니다");
        }
        Client subject = repo.findClientByUsername(form.getUsername()).orElseThrow(NoSuchUserException::new);
//        Client client = Client.builder()
//                .id(subject.getId())
//                .username(form.getUsername())
//                .realName(form.getRealName())
//                .password(form.getPassword())
//                .mobile(form.getMobile())
//                .description(form.getDescription())
//                .build();
//        Client c = mapper.map(form, Client.class);
        mapper.map(form, subject);

//        return repo.save(client);
        return subject;
    }

    // TODO CreationTimeStamp 왜 널이지?
    public Client signupClient(UserForm form) throws SQLIntegrityConstraintViolationException {

        if (checkUsername(form.getUsername())){
            throw new SQLIntegrityConstraintViolationException("아이디가 중복되었습니다");
        }
        Client client = mapper.map(form, Client.class);
        return repo.save(client);
    }

    /**
     *
     * @param username 유저 아이디
     * @return 조회된 유저가 있으면 True
     */
    public boolean checkUsername(String username) {
        return repo.findClientByUsername(username).isPresent();
    }

    @Transactional
    public boolean deleteClient(Long id) throws NoSuchUserException {
        try{
            repo.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new NoSuchUserException("요청하신 정보가 잘못되었습니다");
        }
    }
}
